package com.example.userapp.activity.main.fragments.documents;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;

import com.example.userapp.activity.AbstractViewController;
import com.example.userapp.datamodel.user.UserDataModel;
import com.example.userapp.models.Document;
import com.example.userapp.models.DocumentType;
import com.example.userapp.models.User;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class DocumentsFragmentController extends AbstractViewController  {

    DocumentsFragment documentsFragment;


    DocumentsApiDecorator documentsApiDecorator;

    UserDataModel userDataModel;
    private List<Document> userDocuments;
    private List<DocumentType> validDocumentTypes;

    DocumentsFragmentController(DocumentsFragment documentsFragment) {
        super("DOCUMENTS FRAGMENT CONTROLLER HANDLER THREAD");
        this.documentsFragment = documentsFragment;
        this.documentsApiDecorator = new DocumentsApiDecorator();
        this.userDocuments = new ArrayList<Document>();
        this.validDocumentTypes = new ArrayList<DocumentType>();
        userDataModel = UserDataModel.getInstance();

    }



     void createUploadDialog(Uri uri) {

         Spinner inputSpinnerField = new Spinner(documentsFragment.getContext());

         ArrayAdapter<DocumentType> adapter = new ArrayAdapter<>(documentsFragment.getContext(), android.R.layout.simple_spinner_item, validDocumentTypes);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         inputSpinnerField.setAdapter(adapter);

         AlertDialog dialog = new AlertDialog.Builder(documentsFragment.getContext())
                 .setTitle("Postavljanje dokumenta")
                 .setMessage("Da biste dodali dokument morate da odababerte vrstu dokumenta")
                 .setView(inputSpinnerField)
                 .setPositiveButton("Postavi", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         DocumentType selectedItem = (DocumentType) inputSpinnerField.getSelectedItem();
                         if (selectedItem != null) {
                             // Do something with the selected item
                             uploadSelectedFile(uri, selectedItem);
                         }
                         else
                         {
                             Toast.makeText(documentsFragment.getContext(), "Molimo vas odaberite vrstu dokumenta", Toast.LENGTH_LONG).show();

                         }
                     }
                 })
                 .setNegativeButton("Odustani", null)
                 .setCancelable(false)
                 .create();
         dialog.show();
    }

    private void uploadSelectedFile(Uri uri, DocumentType documentType) {

        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(() -> {
            try {

                ByteBuffer buffer = uriToByteBuffer(uri);
                if (documentsApiDecorator.uploadFile(buffer, documentType))
                {
                    fetchData();
                    Toast.makeText(this.documentsFragment.getContext(), "Dokument je uspjesno postavljen", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                Toast.makeText(this.documentsFragment.getContext(), "Greska pri postavljanju dokuemnta", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                Toast.makeText(this.documentsFragment.getContext(), "Greska pri postavljanju dokuemnta", Toast.LENGTH_SHORT).show();
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
                List<DocumentType> tempValidDocs = this.documentsApiDecorator.getValidDocumentType();
                if(tempValidDocs!=null)
                    this.validDocumentTypes = tempValidDocs;
                userDataModel.updateUserDocuments(this.documentsApiDecorator.getUserDocuments());
                setDataFromDataModel();
            } catch (IOException | JSONException e) {
            } finally {
                if(documentsFragment.getActivity()!=null)
                 documentsFragment.getActivity().runOnUiThread(() -> displayDocumentsUi());
            }

        });
    }







    public void downloadDocument(Document document) {

        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(() -> {
            try {
                ResponseBody responseBody = documentsApiDecorator.downloadODocument(document);
                if (responseBody != null) {
                    File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File outPutFile = new File(downloadDir, document.getDocumentType().getName() + ".pdf");
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

            if (this.userDocuments.size() == 0)
                this.documentsFragment.documentsRv.setVisibility(View.INVISIBLE);
            else {
                if (this.documentsFragment.documentsViewAdapter == null) {
                    documentsFragment.documentsViewAdapter = new DocumentsViewAdapter(this.userDocuments, this);

                    documentsFragment.documentsRv.setAdapter(documentsFragment.documentsViewAdapter);
                } else {

                    documentsFragment.documentsViewAdapter.setData(this.userDocuments);
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
        if (documentsFragment.viewCreated) {
            documentsFragment.progressIndicator.setVisibility(View.INVISIBLE);
            documentsFragment.swiperefreshDocuments.setRefreshing(false);}
    }

    void setDataFromDataModel() {
        List<Document> userDocuments  = userDataModel.getUserDocuments();

        if (userDocuments !=null) {
            this.userDocuments.clear();
            this.userDocuments.addAll(userDocuments);


        }
    }

    public boolean chechData() {
        return this.userDataModel.getUserDocuments()!=null&&this.userDataModel.getUserDocuments().size()!=0;
    }
}
