package com.jmmnt.UseCase.FTP;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.OutputStream;

public class FTPClientFunctions extends AppCompatActivity{
    private final String host = "linux160.unoeuro.com";
    private final String username = "dat32.dk";
    private final String password = "9hkdpBFtAg34";
    private final int port = 21;
    private FTPClient mFTPClient;
    private OutputStream outputStream;

    public boolean ftpConnect(String host, String username, String password, int port) {
        try {
            mFTPClient = new FTPClient();
            mFTPClient.connect(host, port);

            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
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
            e.printStackTrace();
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

    public void ftpDownload(String remotePath, String phoneFileName){
        new Thread(() -> {
            ftpConnect(host, username, password, port);
            boolean status;
            try {
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                File phoneDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File phonePath = new File(phoneDir, phoneFileName);
                outputStream = new BufferedOutputStream(new FileOutputStream(phonePath));
                mFTPClient.retrieveFile(remotePath, outputStream);
                outputStream.close();
                mFTPClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void sendPicToFTP(Bitmap bitmap, String filename,String directory, Context context) {
        new Thread(() -> {
            boolean status;

            status = ftpConnect(host, username, password, port);
            if (status) {
                Log.d(TAG, "Connection Success");

                File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(filepath, filename);

                try {
                    outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                createDirectory(mFTPClient, directory+"/pictures");

                ftpUpload(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        + "/pics/" + filename, filename, directory+"/pictures", null);

                file.delete();

                ftpDisconnect();
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