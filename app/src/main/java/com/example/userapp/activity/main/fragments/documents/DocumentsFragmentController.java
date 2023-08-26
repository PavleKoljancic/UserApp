package com.example.userapp.activity.main.fragments.documents;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;

import com.example.userapp.activity.AbstractViewController;
import com.example.userapp.datamodel.user.UserDataModel;
import com.example.userapp.models.User;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import okhttp3.ResponseBody;

public class DocumentsFragmentController extends AbstractViewController {

    DocumentsFragment documentsFragment;

    ArrayList<String> documentNames;
    DocumentsApiDecorator documentsApiDecorator;

    UserDataModel userDataModel;

    DocumentsFragmentController(DocumentsFragment documentsFragment) {
        super("DOCUMENTS FRAGMENT CONTROLLER HANDLER THREAD");
        this.documentsFragment = documentsFragment;
        this.documentsApiDecorator = new DocumentsApiDecorator();

        documentNames = new ArrayList<>(3);
        userDataModel = UserDataModel.getInstance();
    }



     void createUploadDialog(Uri uri) {
        EditText inputEditTextField = new EditText(documentsFragment.getContext());
        AlertDialog dialog = new AlertDialog.Builder(documentsFragment.getContext())
                .setTitle("Naziv dokumenta")
                .setMessage("Da biste dodali dokument morate mu dati naziv.")
                .setView(inputEditTextField)
                .setPositiveButton("Postavi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String documentName = inputEditTextField.getText().toString();
                        if (documentName != null && !documentName.isEmpty())
                            uploadSelectedFile(uri, documentName);
                    }
                })
                .setNegativeButton("Odustani", null)
                .setCancelable(false)
                .create();
        dialog.show();
    }

    private void uploadSelectedFile(Uri uri, String documentName) {

        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(() -> {
            try {

                ByteBuffer buffer = uriToByteBuffer(uri);
                if (documentsApiDecorator.uploadFile(buffer, documentName))
                    fetchData();

            } catch (JSONException e) {

            } catch (IOException e) {

            }
        });

    }



    private ByteBuffer uriToByteBuffer(Uri uri) throws IOException {
        InputStream inputsream = documentsFragment.getContext().getContentResolver().openInputStream(uri);
        int read = 0;
        ArrayList<byte[]> byteList = new ArrayList<>();
        while (read >= 0) {
            int avialble = inputsream.available();
            if (avialble > 0) {
                byte[] temp = new byte[avialble];
                read = inputsream.read(temp);
                byteList.add(temp);
            } else {
                read = inputsream.read();
                if (read != -1) {
                    byte[] temp = new byte[1];
                    temp[0] = (byte) read;
                    byteList.add(temp);
                }
            }
        }
        inputsream.close();
        int size = 0;
        for (byte[] chunk : byteList)
            size += chunk.length;
        ByteBuffer buffer = ByteBuffer.allocate(size);

        for (byte[] chunk : byteList)
            buffer.put(chunk);
        return buffer;
    }


    public void fetchData() {
        if(documentsFragment.getActivity()!=null)
        documentsFragment.getActivity().runOnUiThread(() -> setLoadingProgressUi());
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(() -> {

            try {
                userDataModel.updateUser(this.documentsApiDecorator.loadUser());
                setDataFromDataModel();
            } catch (IOException | JSONException e) {
            } finally {
                if(documentsFragment.getActivity()!=null)
                documentsFragment.getActivity().runOnUiThread(() -> displayDocumentsUi());
            }

        });
    }



    void setDataFromDataModel() {
        User user = userDataModel.getUser();

        if (user != null) {
            this.documentNames.clear();
            if (user.getDocumentName1() != null)
                this.documentNames.add(user.getDocumentName1());
            if (user.getDocumentName2() != null)
                this.documentNames.add(user.getDocumentName2());
            if (user.getDocumentName3() != null)
                this.documentNames.add(user.getDocumentName3());


        }
    }

    public void deleteDocument(String documentName) {
        Handler handler = new Handler(handlerThread.getLooper());
        setLoadingProgressUi();
        handler.post(() -> {

            try {
                if (this.documentsApiDecorator.deleteDocument(documentName))
                    fetchData();

            } catch (JSONException | IOException e) {
                if(documentsFragment.getActivity()!=null)
                documentsFragment.getActivity().runOnUiThread(() -> setLoadingFinishedUi());
            }

        });

    }

    public void downloadDocument(String documentName) {

        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(() -> {
            try {
                ResponseBody responseBody = documentsApiDecorator.downloadODocument(documentName);
                if (responseBody != null) {
                    File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File outPutFile = new File(downloadDir, documentName + ".pdf");
                    FileOutputStream fileOutputStream = new FileOutputStream(outPutFile);
                    fileOutputStream.write(responseBody.bytes());
                }
            } catch (JSONException e) {

            } catch (IOException e) {

            }


        });

    }

    void displayDocumentsUi() {
        if (documentsFragment.viewCreated) {
            setLoadingFinishedUi();
            if (documentNames.size() >= 3)
                documentsFragment.uploadbtn.setVisibility(View.INVISIBLE);
            else
                documentsFragment.uploadbtn.setVisibility(View.VISIBLE);
            if (this.documentNames.size() == 0)
                this.documentsFragment.documentsRv.setVisibility(View.INVISIBLE);
            else {
                if (this.documentsFragment.documentsViewAdapter == null) {
                    documentsFragment.documentsViewAdapter = new DocumentsViewAdapter(this.documentNames, this);

                    documentsFragment.documentsRv.setAdapter(documentsFragment.documentsViewAdapter);
                } else {

                    documentsFragment.documentsViewAdapter.setData(this.documentNames);
                    documentsFragment.documentsViewAdapter.notifyDataSetChanged();

                    if (documentsFragment.documentsRv.getAdapter() == null)
                        documentsFragment.documentsRv.setAdapter(documentsFragment.documentsViewAdapter);
                }
                this.documentsFragment.documentsRv.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setLoadingProgressUi() {
        if (documentsFragment.viewCreated)
            documentsFragment.progressIndicator.setVisibility(View.VISIBLE);
    }

    private void setLoadingFinishedUi() {
        if (documentsFragment.viewCreated)
            documentsFragment.progressIndicator.setVisibility(View.INVISIBLE);
            documentsFragment.swiperefreshDocuments.setRefreshing(false);
    }
}
