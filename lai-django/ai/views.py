from django.conf import settings
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import IsAuthenticated

import openai

# Create your views here.

openai.api_key = settings.OPENAI_API_KEY

client = openai.OpenAI()

MODEL_NAME = "gpt-4o-mini"


def system_response(language):
    return f"You must answer the requests in {language} and your responses must be accurate"


class GetTitle(APIView):
    """
    API endpoint to generate a title based on style and theme.
    """

    permission_classes = [IsAuthenticated]

    def post(self, request, *args, **kwargs):
        try:
            language = request.data.get("language", "Spanish")
            style = request.data.get("style", "professional")
            theme = request.data.get("theme")

            if not theme:
                return Response(
                    {"error": "theme are required"},
                    status=status.HTTP_400_BAD_REQUEST,
                )

            response = client.chat.completions.create(
                model=MODEL_NAME,
                messages=[
                    {
                        "role": "system",
                        "content": f"You are a copywriter. {system_response(language)}",
                    },
                    {
                        "role": "system",
                        "content": f"You just have to answer the title",
                    },
                    {
                        "role": "user",
                        "content": f"Give me a {style} title for a {theme} slide.",
                    },
                ],
            )
            title = response.choices[0].message.content
            return Response({"title": title}, status=status.HTTP_200_OK)

        except Exception as e:
            return Response(
                {"error": str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR
            )


class GetRoadmap(APIView):
    """
    API endpoint to generate a roadmap based on theme and number of items.
    """

    permission_classes = [IsAuthenticated]

    def post(self, request, *args, **kwargs):
        try:
            language = request.data.get("language", "Spanish")
            theme = request.data.get("theme")
            number = request.data.get("number", 5)

            if not theme:
                return Response(
                    {"error": "theme is required"}, status=status.HTTP_400_BAD_REQUEST
                )

            response = client.chat.completions.create(
                model=MODEL_NAME,
                messages=[
                    {
                        "role": "system",
                        "content": f"You are an {theme} assistant. {system_response(language)}",
                    },
                    {
                        "role": "system",
                        "content": f"You respond a list in the following format:\n\n1. name of the n°1 topic theme\n2. name of the n°2 topic\n...\n{number}. name of the topic°{number}",
                    },
                    {
                        "role": "user",
                        "content": f"Give me a roadmap of {number} items for a {theme} presentation",
                    },
                ],
            )
            roadmap = response.choices[0].message.content

            topics = []
            lines = roadmap.split("\n")
            for line in lines:
                stripped_line = line.strip()
                if stripped_line.startswith(tuple(map(str, range(1, 10)))):
                    _, content = stripped_line.split(". ", 1)
                    topics.append(content)
            return Response({"roadmap": topics}, status=status.HTTP_200_OK)

        except Exception as e:
            return Response(
                {"error": str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR
            )


class GetSyllabus(APIView):
    """
    API endpoint to generate a syllabus based on topic and number of items.
    """

    permission_classes = [IsAuthenticated]

    def post(self, request, *args, **kwargs):
        try:
            language = request.data.get("language", "Spanish")
            theme = request.data.get("theme")
            roadmap = request.data.get("roadmap")
            number = request.data.get("number", 5)

            if not roadmap:
                return Response(
                    {"error": "topic is required"}, status=status.HTTP_400_BAD_REQUEST
                )

            subtopics_list = []

            for i in roadmap:
                new_message = {
                    "role": "user",
                    "content": f"Give me a syllabus of {number} subtopics for the following topic: {i}",
                }
                messages = [
                    {
                        "role": "system",
                        "content": f"You are an {theme} assistant. {system_response(language)}",
                    },
                    {
                        "role": "system",
                        "content": f"You respond a list about {theme} in the following format:\n\n1. name of the topic 1\n2. name of the topic 2\n...\n{number}. name of the topic {number}",
                    },
                ]
                messages.append(new_message)

                response = client.chat.completions.create(
                    model=MODEL_NAME, messages=messages
                )

                syllabus = response.choices[0].message.content

                subtopics = []

                lines = syllabus.split("\n")
                for line in lines:
                    stripped_line = line.strip()
                    if stripped_line.startswith(tuple(map(str, range(1, 10)))):
                        _, content = stripped_line.split(". ", 1)
                        subtopics.append(content)

                subtopics_list.append(subtopics)

                assistant_response = {
                    "role": "assistant",
                    "content": syllabus,
                }

                messages.append(assistant_response)

            return Response({"syllabus": subtopics_list}, status=status.HTTP_200_OK)

        except Exception as e:
            return Response(
                {"error": str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR
            )


class GetSummary(APIView):
    """
    API endpoint to generate a summary based on subtopic.
    """

    def post(self, request, *args, **kwargs):
        try:
            language = request.data.get("language", "Spanish")
            topic = request.data.get("topic")
            subtopic = request.data.get("subtopic")

            if not topic or not subtopic:
                return Response(
                    {"error": "topic and subtopic are required"},
                    status=status.HTTP_400_BAD_REQUEST,
                )

            response = client.chat.completions.create(
                model=MODEL_NAME,
                messages=[
                    {
                        "role": "system",
                        "content": f"You are an {subtopic} assistant. {system_response(language)}",
                    },
                    {
                        "role": "assistant",
                        "content": f"Give me a brief summary of a 2-line paragraph to learn {subtopic} of the following {topic}",
                    },
                ],
            )
            summary = response.choices[0].message.content
            return Response({"summary": summary}, status=status.HTTP_200_OK)

        except Exception as e:
            return Response(
                {"error": str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR
            )
