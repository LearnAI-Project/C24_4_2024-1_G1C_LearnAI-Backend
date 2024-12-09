from rest_framework import authentication
from rest_framework import exceptions
from django.contrib.auth.models import User
import jwt
from django.conf import settings

class SpringJWTAuthentication(authentication.BaseAuthentication):
    def authenticate(self, request):
        auth_header = authentication.get_authorization_header(request).split()

        if not auth_header or auth_header[0].lower() != b'bearer':
            return None

        if len(auth_header) == 1:
            msg = 'Token inválido: falta el token.'
            raise exceptions.AuthenticationFailed(msg)
        elif len(auth_header) > 2:
            msg = 'Token inválido: cabecera de autorización malformada.'
            raise exceptions.AuthenticationFailed(msg)

        try:
            token = auth_header[1]
            if isinstance(token, bytes):
                token = token.decode('utf-8')

            # Decodifica el token sin verificar la firma primero para extraer el payload
            unverified_payload = jwt.decode(token, options={"verify_signature": False})
            username = unverified_payload.get('sub')

            if username is None:
                raise exceptions.AuthenticationFailed('Token inválido: no se encontró el usuario.')

            # Verificar la firma del token
            payload = jwt.decode(token, settings.SIMPLE_JWT['SIGNING_KEY'], algorithms=[settings.SIMPLE_JWT['ALGORITHM']])

            # Obtener o crear el usuario en Django
            user, _ = User.objects.get_or_create(username=username)

            return (user, None)
        except jwt.ExpiredSignatureError:
            raise exceptions.AuthenticationFailed('Token ha expirado.')
        except jwt.InvalidTokenError:
            raise exceptions.AuthenticationFailed('Token inválido.')