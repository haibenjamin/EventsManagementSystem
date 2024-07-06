# Event Planner Mobile Client

This repository contains the mobile client application for the Event Planner system. The client is written in Java and interfaces with an Express.js and React.js server for managing various event-related functionalities such as event creation, guest list management, task lists, vendor management, user authentication, and more.

## Features

- **User Authentication:**
  - Login
  - Logout
  - Register

- **Event Management:**
  - Create Event
  - Delete Event
  - Update Event Details

- **Guest List Management:**
  - Add Guests
  - Remove Guests
  - View Guest List

- **Task Lists:**
  - Manage Tasks for Events
  - Mark Tasks as Complete
  - Assign Tasks to Users

- **Vendor Management:**
  - Add Vendors
  - Remove Vendors
  - View Vendor Details
  - Contact and Negotiate Vendor
    

- **Network Communication:**
  - Uses Retrofit for API communication
  - OkHttp for handling HTTP requests


## Installation

To run the Event Planner mobile client locally, follow these steps:

- **Clone the repository:**

   ```bash
   git clone https://github.com/LiadOvdat5/Event-Planner.git

   Open the project in Android Studio:

- **Launch Android Studio.**
 - Choose "Open an existing Android Studio project."
 - Navigate to the directory where you cloned the repository and select the Event-Planner directory.
  
- **Build and Run:**
 - Build the project using Android Studio.
 - Run the application on an emulator or a physical device.

  
## Configuration

- Update the server endpoint in ApiClient.java file to point to your Express and React server.
- Ensure the server is running and accessible from the mobile client.

  
## Dependencies

- Retrofit: A type-safe HTTP client for Android and Java.
- OkHttp: An HTTP client for Android and Java applications.

  
## Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

