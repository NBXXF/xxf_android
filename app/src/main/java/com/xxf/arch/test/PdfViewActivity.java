package com.xxf.arch.test;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class PdfViewActivity extends AppCompatActivity {

//    PDFView pdfView;

    PdfRenderer pdfRenderer;
    PdfRenderer.Page currpage;
    ParcelFileDescriptor parcelFileDescriptor;
    int pageindex;
//    SubsamplingScaleImageView imagepdf;
//    ImageView imagepdf;
//    FloatingActionButton /*imagepdf,*/prevbtn,nextbtn;
    String viewtype,pdffile;

    RecyclerView recyclerView;

    ArrayList<Bitmap> arrayList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

//        imagepdf = findViewById(R.id.imagepdf);
//        prevbtn = findViewById(R.id.prevbtn);
//        nextbtn = findViewById(R.id.nextbtn);
//        pdfView = findViewById(R.id.pdfview);
        recyclerView = findViewById(R.id.recyclerview);


        pageindex = 0;

        if (getIntent()!=null){
           viewtype = getIntent().getStringExtra("Viewtype");

            if (viewtype != null && viewtype.equals("storage")) {

               pdffile = (getIntent().getStringExtra("Fileuri"));
//              pdffile =  pdfuri.getPath();
//                Log.d("path",pdffile);
                Log.d("viewtype",pdffile);

                openRenderer(this);




                //                Uri pdffile = Uri.parse(getIntent().getStringExtra("Fileuri"));

                  /*  pdfView.fromUri(pdffile)
                            .onRender(new OnRenderListener() {
                                @Override
                                public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                                    pdfView.fitToWidth();
                                }
                            })
                            .enableAnnotationRendering(true)
                            .invalidPageColor(Color.RED)
                            .load();
                */
            }


        }

        /*prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPage(currpage.getIndex()-1);
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPage(currpage.getIndex()+1);
            }
        });
    */}

    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    protected void onStop() {
//        closeRenderer();
        super.onStop();
    }

    private void updateUi() {

        int index = currpage.getIndex();
        int pagecount = pdfRenderer.getPageCount();
//        prevbtn.setEnabled(0!=index);
//        nextbtn.setEnabled(index+1<pagecount);
    }

    private void closeRenderer() {

        pdfRenderer.close();
        try {
            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openRenderer(PdfViewActivity pdfViewActivity) {

        File file = new File(pdffile);

        try {
            parcelFileDescriptor = ParcelFileDescriptor.open(file,ParcelFileDescriptor.MODE_READ_ONLY);

            if (parcelFileDescriptor!=null){
                pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                showPage();
            }
        } catch (IOException e) {
            Log.d("IOError", e.toString());
            e.printStackTrace();
        }
    }

    private void showPage() {
      /*  if (pdfRenderer.getPageCount()<=pageindex) {
            return;
        }
      */ /* if (null!=currpage){
            currpage.close();
        }*/

        for (int pageindex = 0;pageindex<pdfRenderer.getPageCount();pageindex++) {

//            if (pdfRenderer.getPageCount()<=pageindex) {
                currpage = pdfRenderer.openPage(pageindex);

                Bitmap bitmap = Bitmap.createBitmap(currpage.getWidth(), currpage.getHeight(), Bitmap.Config.ARGB_8888);

                currpage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
//        imagepdf.setImage(ImageSource.bitmap(bitmap));
//        imagepdf.setImageBitmap(bitmap);
                arrayList.add(bitmap);
                currpage.close();

//            }
//            updateUi();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(PdfViewActivity.this));
        recyclerView.setAdapter(new RecyclerviewAdapter(PdfViewActivity.this,arrayList));
    }

}
