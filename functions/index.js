// Import the necessary Firebase modules
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

// Initialize Firestore and Messaging services
const firestore = admin.firestore();
const messaging = admin.messaging();

// Cloud Function to send a notification to all users when a new news item is added
exports.sendNewsNotificationToAllUsers = functions.firestore.document('MedicalNews/{newsId}')
  .onCreate(async (snapshot, context) => {
    const newsData = snapshot.data(); // Access the newly created news document data

    try {
        const usersSnapshot = await firestore.collection('users').get();
        const tokens = usersSnapshot.docs
          .map(doc => doc.data().realtimeid) // Extract the realtimeid
          .filter(token => token != null); // Ensure the token exists

        if (tokens.length > 0) {
            const message = {
                notification: {
                    title: newsData.Title, // Assuming the news document has a Title field
                    body: 'A new news item has been posted. Check it out!'
                },
                tokens: tokens
            };

            const response = await messaging.sendMulticast(message);
            console.log('Successfully sent message:', response.successCount);
        } else {
            console.log('No FCM tokens found.');
        }
    } catch (error) {
        console.error('Error sending notification:', error);
    }
});
