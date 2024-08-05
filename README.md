# Synapse

**Synapse** is a modern social media application designed to bring together features commonly found in popular platforms like Facebook and Instagram. It provides a comprehensive range of functionalities, including user profile management, notifications, search, and interactive content browsing.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Screenshots](#screenshots)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Features

- **User Profile Management**
  - View and update profile picture, name, and occupation
  - Manage personal settings and preferences

- **Notifications**
  - Receive and view notifications for new interactions and updates

- **Search Functionality**
  - Search for posts, users, and other content

- **Interactive Feed**
  - Browse through posts, photos, and updates from your network

- **Content Sharing**
  - Share posts, photos, and status updates

- **Messaging**
  - Chat with friends and other users directly within the app

- **Explore Section**
  - Discover new content and users through curated categories

- **Settings**
  - Customize app preferences and manage account settings

## Technologies Used

- **Java**: Main programming language used for developing the application logic
- **Firebase**: Backend services including Firestore for user data storage and retrieval
- **XML**: Layouts and user interface design
- **RecyclerView**: For displaying scrollable lists
- **SharedPreferences**: For storing user session data
- **AndroidX Libraries**: Modern Android development components

## Screenshots

- Will be Added soon!!

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/username/synapse.git
    cd synapse
    ```

2. Open the project in Android Studio

3. Ensure you have the necessary dependencies installed

4. Build and run the application on an Android device or emulator

## Usage

- **User Profile Management**
  - Access and update your profile from the profile section

- **Notifications**
  - Tap the notification button to view recent notifications

- **Search**
  - Use the search bar to find users, posts, and other content

- **Interactive Feed**
  - Scroll through your feed to see updates and posts from your network

- **Content Sharing**
  - Share new posts and photos using the sharing feature

- **Messaging**
  - Start conversations with other users from the messaging section

- **Explore Section**
  - Browse through various categories to discover new content

## Contributing

- Fork the repository
- Create your feature branch:
    ```sh
    git checkout -b feature/AmazingFeature
    ```

- Commit your changes:
    ```sh
    git commit -m 'Add some AmazingFeature'
    ```

- Push to the branch:
    ```sh
    git push origin feature/AmazingFeature
    ```

- Open a pull request

## License

- Distributed under the MIT License. See `LICENSE` for more information

## Acknowledgments

- Thanks to the open-source community for providing valuable libraries and tools
- Special thanks to the contributors and testers who helped improve the app

---

## Additional Details

- **Dependencies**
  - Firebase Firestore for user data storage and retrieval
  - AndroidX libraries for modern Android development components
  - RecyclerView for displaying scrollable lists
  - SharedPreferences for storing user session data

- **File Structure**

    ```
    synapse/
    │
    ├── .github/
    │   └── workflows/
    │       └── loc.yml            # GitHub Actions workflow for counting lines of code
    │
    ├── app/
    │   ├── src/
    │   │   ├── main/
    │   │   │   ├── java/
    │   │   │   │   └── com/example/synapse/
    │   │   │   │       ├── Adapter/
    │   │   │   │       │   └── OptionAdapter.java
    │   │   │   │       ├── utils/
    │   │   │   │       │   └── AndroidUtil.java
    │   │   │   │       ├── MainActivity.java
    │   │   │   │       └── ... (other activities)
    │   │   │   ├── res/
    │   │   │   │   ├── layout/
    │   │   │   │   │   ├── activity_main.xml
    │   │   │   │   │   ├── item_option.xml
    │   │   │   │   │   └── ... (other layouts)
    │   │   │   │   ├── drawable/
    │   │   │   │   │   ├── option_background.xml
    │   │   │   │   │   └── ... (other drawables)
    │   │   │   │   ├── values/
    │   │   │   │   │   └── colors.xml
    │   │   │   │   │   └── ... (other values)
    │   │   │   │   └── ... (other resources)
    │   │   │   └── AndroidManifest.xml
    │   └── build.gradle
    │
    └── README.md
    ```

