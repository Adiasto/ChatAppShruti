const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/notifications/{userId}/{notificationId}')
.onWrite(event=>{
        const params=event.after._path.split('/');
        const userId=params[2];
        const messageId=params[3];

        const notificationInfo=admin.database().ref('/notifications/'+userId+'/'+messageId+'/from').once('value');
        return notificationInfo.then(
            notification=>{
                const senderId=notification.val();
                const senderInfo=admin.database().ref('/users/'+senderId+'/username').once('value')
                return senderInfo.then(
                    senderVal=>{
                        const senderName=senderVal.val();
                        const messageInfo=admin.database().ref('/chats/'+senderId+'/'+userId+'/'+messageId+'/message').once('value');
                        return messageInfo.then(
                            messageVal=>{
                                const message=messageVal.val();
                                const tokenId=admin.database().ref('/users/'+userId+'/tokenId').once('value')
                                return tokenId.then(event=>{
                                    const deviceToken=event.val();
                                    const payload={
                                        notification:{
                                            title:''+senderName,
                                            body:''+message,
                                            icon:"default"
                                        }
                                    };
                                    console.log("device token",deviceToken);
                                    return admin.messaging().sendToDevice(deviceToken,payload).then(
                                        response=>{
                                            console.log("notification sent");
                                        }
                                    )
                                })
                            }
                        )
                    }
                )

                
            }
        )


       
    }
)