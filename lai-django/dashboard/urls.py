from django.urls import path
from .views import login, profile

urlpatterns = [
    path('login/', login, name='login'),
    path('profile/', profile, name='profile'),
]