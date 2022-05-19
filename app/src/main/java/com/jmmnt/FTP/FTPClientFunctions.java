package com.jmmnt.FTP;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FTPClientFunctions extends AppCompatActivity{
    public FTPClient mFTPClient = null;
    OutputStream outputStream;

    public boolean ftpConnect(String host, String username, String password, int port) {
        try {
            mFTPClient = new FTPClient();
            // connecting to the host
            mFTPClient.connect(host, port);
            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                // login using username & password
                boolean status = mFTPClient.login(username, password);
                /*
                 * Set File Transfer Mode
                 * To avoid corruption issue you must specified a correct
                 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
                 * transferring text, image, and compressed files.
                 */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
                return status;
            }
        } catch (Exception e) {
            Log.d(TAG, "Error: could not connect to host " + host);
        }
        return false;
    }

    public boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
        }
        return false;
    }

    public boolean ftpUpload(String srcFilePath, String desFileName, String desDirectory, Context context) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFilePath);
            // change working directory to the destination directory
            // if (ftpChangeDirectory(desDirectory)) {
            status = mFTPClient.storeFile(desFileName, srcFileStream);
            // }
            srcFileStream.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "upload failed: " + e);
        }
        return status;
    }

    public void sendPicToFTP(Bitmap bitmap, String filename,String directory, Context context) {
        new Thread(new Runnable() {
            public void run() {
                boolean status = false;
                // host – your FTP address
                // username & password – for your secured login
                // 21 default gateway for FTP
                String host = "linux160.unoeuro.com";
                String username = "dat32.dk";
                String password = "9hkdpBFtAg34";
                int port = 21;

                status = ftpConnect(host, username, password, port);
                if (status) {
                    Log.d(TAG, "Connection Success");

                    //File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(filepath, filename);
                    try {
                        outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        //os.write(fileContent.getBytes());
                        //os.close();
                        //System.out.println("wrote the file to download folder");
                    } catch (IOException e) {
                        System.out.println("error writing the file to disk");
                        e.printStackTrace();
                    }

                    //Toast.makeText(getApplicationContext(), "Image saved to download folder.",Toast.LENGTH_LONG).show();

                    try {
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    createDirectory(mFTPClient, directory+"/pictures");
                    ftpUpload(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            + "/pics/" + filename, filename, directory+"/pictures", null);
                    file.delete();
                    ftpDisconnect();
                } else {
                    Log.d(TAG, "Connection failed");
                }
            }
        }).start();
    }

    public void createDirectory(FTPClient client, String directory){
        boolean doesDirectoryExist = false;

            try {
                doesDirectoryExist = client.changeWorkingDirectory(directory);
                System.out.println("created");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(!doesDirectoryExist){
                try {
                    client.makeDirectory(directory);
                    client.changeWorkingDirectory(directory);
                    System.out.println("already exists");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }

}