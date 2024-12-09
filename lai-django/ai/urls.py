from django.urls import path
from .views import GetTitle, GetRoadmap, GetSyllabus, GetSummary

urlpatterns = [
    path('get_title/', GetTitle.as_view(), name='get_title'),
    path('get_roadmap/', GetRoadmap.as_view(), name='get_roadmap'),
    path('get_syllabus/', GetSyllabus.as_view(), name='get_syllabus'),
    path('get_summary/', GetSummary.as_view(), name='get_summary'),
]