# Simple Quiz Application with Steganography, Faculty of Electrical Engineering, 2022

Application specification

This application allows users to participate in a simple quiz with 20 questions. 
Users can select 5 randomly chosen questions from the question bank. 
The questions can either require entering the correct answer or selecting one of the four provided options. 
All questions are stored as images using steganography techniques to prevent their discovery outside the application.

Features

    User Registration: Users can register by providing a username and password. Upon registration, a digital certificate is automatically generated for the user, linked to their user data.
    System Login: Users can log in to the system using the generated certificate.
    Displaying Questions: After logging in, users are presented with the selected questions one by one.
    Steganography: Questions are stored as images using steganographic algorithms to prevent their discovery outside the application. The image quality is minimally affected to preserve readability.
    Results: The results of all users are stored in a separate text file in the format "USERNAME TIME RESULT." Only logged-in users can access the content of this file within the application.
    CA Infrastructure: Using OpenSSL or another tool, the CA infrastructure is implemented with a ROOT CA and subordinate CA bodies. Subordinate CAs issue digital certificates for quiz participants and CRL lists for certificate revocation.
