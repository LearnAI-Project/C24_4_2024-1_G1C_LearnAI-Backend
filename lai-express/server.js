import express from 'express';
import cors from 'cors';
import { readFileSync, unlinkSync } from 'fs';
import { fileURLToPath } from 'url';
import { dirname } from 'path';
import puppeteer from 'puppeteer';
import { Marp } from '@marp-team/marp-core';
import { initializeApp } from 'firebase/app';
import { getStorage, ref, uploadBytes, getDownloadURL, listAll } from 'firebase/storage';
import dotenv from 'dotenv';

dotenv.config();

const HOST_FRONTEND = process.env.HOST_FRONTEND;
const PORT = process.env.PORT || 3000;


const app = express();
app.use(cors({ origin: `${HOST_FRONTEND}` }));
app.use(express.text());

const firebaseConfig = {
    apiKey: process.env.FIREBASE_API_KEY,
    authDomain: process.env.FIREBASE_AUTH_DOMAIN,
    projectId: process.env.FIREBASE_PROJECT_ID,
    storageBucket: process.env.FIREBASE_STORAGE_BUCKET,
    messagingSenderId: process.env.FIREBASE_MESSAGING_SENDER_ID,
    appId: process.env.FIREBASE_APP_ID,
    measurementId: process.env.FIREBASE_MEASUREMENT_ID
};

const firebaseApp = initializeApp(firebaseConfig);
const storage = getStorage(firebaseApp);

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const generateUniqueFileName = async (storage, baseName, extension) => {
    let fileName = `${baseName}.${extension}`;
    let counter = 1;
    const storageRef = ref(storage, `pdfs/${fileName}`);
    let fileExists = await fileExistsInStorage(storageRef);

    while (fileExists) {
        fileName = `${baseName}${counter}.${extension}`;
        counter++;
        const newStorageRef = ref(storage, `pdfs/${fileName}`);
        fileExists = await fileExistsInStorage(newStorageRef);
    }

    return fileName;
};

const fileExistsInStorage = async (storageRef) => {
    try {
        await getDownloadURL(storageRef);
        return true;
    } catch (error) {
        if (error.code === 'storage/object-not-found') {
            return false;
        }
        throw error;
    }
};

app.post('/convert', async (req, res) => {
    const markdown = req.body;
    const { format = 'pdf' } = req.query;

    if (!markdown) {
        console.log('No markdown content provided');
        return res.status(400).json({ error: 'El campo "markdown" es obligatorio.' });
    }

    const marpit = new Marp();
    const { html, css } = marpit.render(markdown);

    const htmlContent = `
      <!DOCTYPE html>
      <html>
        <head>
          <style>${css}</style>
        </head>
        <body>
          ${html}
        </body>
      </html>
    `;

    const baseName = 'output';
    const extension = format;
    const outputFilePath = await generateUniqueFileName(storage, baseName, extension);

    try {
        console.log('Launching Puppeteer');
        const browser = await puppeteer.launch({
            args: ['--no-sandbox', '--disable-setuid-sandbox']
        });
        const page = await browser.newPage();

        console.log('Setting page content');
        await page.setContent(htmlContent);

        console.log('Generating PDF');
        await page.pdf({ path: outputFilePath, format: 'A4' });

        console.log('Closing browser');
        await browser.close();

        console.log('Conversion complete:', outputFilePath);

        try {
            console.log('Uploading file to Firebase Storage');
            const fileBuffer = readFileSync(outputFilePath);
            const storageRef = ref(storage, `pdfs/${outputFilePath}`);
            await uploadBytes(storageRef, fileBuffer);

            const downloadURL = await getDownloadURL(storageRef);
            console.log('File uploaded to Firebase:', downloadURL);

            unlinkSync(outputFilePath);
            console.log('Temporary files deleted');

            res.json({ url: downloadURL });
        } catch (uploadError) {
            console.error('Error uploading to Firebase:', uploadError);
            res.status(500).json({ error: 'Error uploading to Firebase.' });
        }
    } catch (error) {
        console.error('Error:', error);
        res.status(500).json({ error: 'Ocurrió un error durante la conversión o la subida.' });
    }
});

app.listen(`${PORT}`, () => console.log('Server running on port:', `${PORT}`));