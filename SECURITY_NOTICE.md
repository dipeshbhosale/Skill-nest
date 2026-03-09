# ⚠️ CRITICAL SECURITY NOTICE ⚠️

## Firebase Service Account Credentials Were Exposed

Your Firebase service account credentials were shared in plain text and **MUST BE ROTATED IMMEDIATELY** to prevent unauthorized access to your Firebase project.

## Immediate Actions Required

### 1. Rotate the Compromised Service Account Key

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project: `tick-c8465`
3. Navigate to: **Project Settings** > **Service Accounts**
4. Click on the service account: `firebase-adminsdk-fbsvc@tick-c8465.iam.gserviceaccount.com`
5. Delete the compromised key (ID: `320006eaa667ec002b96efdfcb2c3f2d73ad1d2e`)
6. Generate a new private key
7. Download the new JSON key file
8. Replace the contents of `firebase-service-account.json` files in:
   - `backend/auth-service/src/main/resources/`
   - `backend/user-service/src/main/resources/`
   - `backend/course-service/src/main/resources/`
   - `backend/assignment-service/src/main/resources/`

### 2. Configure Frontend Firebase Credentials

The frontend needs web app credentials (different from service account):

1. Go to Firebase Console > Project Settings > General
2. Scroll to "Your apps" section
3. If no web app exists, click "Add app" > Web
4. Copy the configuration values
5. Update `frontend/.env` with the actual values:
   ```
   VITE_FIREBASE_API_KEY=your_actual_api_key
   VITE_FIREBASE_AUTH_DOMAIN=tick-c8465.firebaseapp.com
   VITE_FIREBASE_PROJECT_ID=tick-c8465
   VITE_FIREBASE_STORAGE_BUCKET=tick-c8465.firebasestorage.app
   VITE_FIREBASE_MESSAGING_SENDER_ID=your_actual_sender_id
   VITE_FIREBASE_APP_ID=your_actual_app_id
   ```

### 3. Verify Security

After rotating credentials:

1. Check Firebase Console > Authentication > Users - look for unauthorized access
2. Review Firebase Console > Firestore Database - check for unexpected data changes
3. Monitor Firebase Console > Usage for unusual activity
4. Consider enabling Firebase App Check for additional security

## Current Setup

Your credentials are now properly configured but **need to be replaced**:

- ✅ Service account files created in each backend service
- ✅ Files are excluded from git via `.gitignore` 
- ✅ Frontend environment variables template created
- ⚠️ **CREDENTIALS MUST BE ROTATED** due to exposure

## Security Best Practices Going Forward

1. **Never share credentials** in:
   - Chat messages
   - Code repositories
   - Screenshots
   - Documentation
   - Slack/Teams messages

2. **Use environment variables** for sensitive data

3. **Rotate credentials** regularly (every 90 days recommended)

4. **Enable Firebase Security Rules** for Firestore and Storage

5. **Use principle of least privilege** - create service accounts with minimal required permissions

6. **Monitor access logs** regularly in Firebase Console

## Need Help?

- [Firebase Security Best Practices](https://firebase.google.com/docs/projects/provisioning/best-practices)
- [Managing Service Accounts](https://cloud.google.com/iam/docs/service-accounts)
- [Firebase App Check](https://firebase.google.com/docs/app-check)
