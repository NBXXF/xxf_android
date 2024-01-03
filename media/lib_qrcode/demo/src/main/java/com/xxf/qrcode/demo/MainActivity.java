package com.xxf.qrcode.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.xch.generateqrcode.R;
import com.xxf.qrcode.QRCodeProviders;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_generate;
    private EditText et_content;
    private ImageView iv_qrcode;
    private ImageView picture_logo, picture_black;//logo，代替黑色色块的图片

    private EditText et_width, et_height;
    private Spinner sp_error_correction_level, sp_margin, sp_color_black, sp_color_white;
    private String content;//二维码内容
    private int width, height;//宽度，高度
    private String error_correction_level, margin;//容错率，空白边距
    private int color_black, color_white;//黑色色块，白色色块

    public static final int TAKE_PHOTO = 1;//拍照
    public static final int CHOOSE_PHOTO = 2;//从相册选择图片
    private Uri imageUri;
    private Bitmap logoBitmap;//logo图片
    private Bitmap blackBitmap;//代替黑色色块的图片
    private int remark;//标记返回的是logo还是代替黑色色块图片

    private Bitmap qrcode_bitmap;//生成的二维码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        et_content = findViewById(R.id.et_content);
        btn_generate = findViewById(R.id.btn_generate);
        iv_qrcode = findViewById(R.id.iv_qrcode);
        picture_logo = findViewById(R.id.picture_logo);
        picture_black = findViewById(R.id.picture_black);
        et_width = findViewById(R.id.et_width);
        et_height = findViewById(R.id.et_height);
        sp_error_correction_level = findViewById(R.id.sp_error_correction_level);
        sp_margin = findViewById(R.id.sp_margin);
        sp_color_black = findViewById(R.id.sp_color_black);
        sp_color_white = findViewById(R.id.sp_color_white);
        btn_generate.setOnClickListener(this);
        picture_logo.setOnClickListener(this);
        picture_black.setOnClickListener(this);
        iv_qrcode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                imgChooseDialog();
                return true;
            }
        });

        sp_error_correction_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                error_correction_level = getResources().getStringArray(R.array.spinarr_error_correction)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_margin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                margin = getResources().getStringArray(R.array.spinarr_margin)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_color_black.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str_color_black = getResources().getStringArray(R.array.spinarr_color_black)[position];
                if (str_color_black.equals("黑色")) {
                    color_black = Color.BLACK;
                } else if (str_color_black.equals("白色")) {
                    color_black = Color.WHITE;
                } else if (str_color_black.equals("蓝色")) {
                    color_black = Color.BLUE;
                } else if (str_color_black.equals("绿色")) {
                    color_black = Color.GREEN;
                } else if (str_color_black.equals("黄色")) {
                    color_black = Color.YELLOW;
                } else if (str_color_black.equals("红色")) {
                    color_black = Color.RED;
                } else if (str_color_black.equals("紫色")) {
                    color_black = Color.parseColor("#9370DB");
                } else if (str_color_black.equals("粉红色")) {
                    color_black = Color.parseColor("#ffc0cb");
                } else if (str_color_black.equals("薄荷色")) {
                    color_black = Color.parseColor("#BDFCC9");
                } else {
                    color_black = Color.BLACK;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_color_white.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str_color_white = getResources().getStringArray(R.array.spinarr_color_white)[position];
                if (str_color_white.equals("黑色")) {
                    color_white = Color.BLACK;
                } else if (str_color_white.equals("白色")) {
                    color_white = Color.WHITE;
                } else if (str_color_white.equals("蓝色")) {
                    color_white = Color.BLUE;
                } else if (str_color_white.equals("绿色")) {
                    color_white = Color.GREEN;
                } else if (str_color_white.equals("黄色")) {
                    color_white = Color.YELLOW;
                } else if (str_color_white.equals("红色")) {
                    color_white = Color.RED;
                } else if (str_color_white.equals("紫色")) {
                    color_white = Color.parseColor("#9370DB");
                } else if (str_color_white.equals("粉红色")) {
                    color_white = Color.parseColor("#ffc0cb");
                } else if (str_color_white.equals("薄荷色")) {
                    color_white = Color.parseColor("#BDFCC9");
                } else {
                    color_white = Color.WHITE;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_generate://生成
                generateQrcodeAndDisplay();
                break;
            case R.id.picture_logo://选取logo
                remark = 0;
                showChooseDialog();
                break;
            case R.id.picture_black://选取代替黑色色块的图片
                remark = 1;
                showChooseDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 保存图片至本地
     *
     * @param bitmap
     */
    private void saveImg(Bitmap bitmap) {
        String fileName = "qr_" + System.currentTimeMillis() + ".jpg";
        boolean isSaveSuccess = ImageUtil.saveImageToGallery(MainActivity.this, bitmap, fileName);
        if (isSaveSuccess) {
            Toast.makeText(MainActivity.this, "图片已保存至本地", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "保存图片失败，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 分享图片(直接将bitamp转换为Uri)
     *
     * @param bitmap
     */
    private void shareImg(Bitmap bitmap) {
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");//设置分享内容的类型
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent = Intent.createChooser(intent, "分享");
        startActivity(intent);
    }

    /**
     * 生成二维码并显示
     */
    private void generateQrcodeAndDisplay() {
        content = et_content.getText().toString();
        String str_width = et_width.getText().toString();
        String str_height = et_height.getText().toString();
        if (str_width.length() <= 0 || str_height.length() <= 0) {
            width = 650;
            height = 650;
        } else {
            width = Integer.parseInt(str_width);
            height = Integer.parseInt(str_height);
        }

        if (content.length() <= 0) {
            Toast.makeText(this, "你没有输入二维码内容哟！", Toast.LENGTH_SHORT).show();
            return;
        }
        qrcode_bitmap = QRCodeProviders.of(content)
                .setOutputSize(new Size(width, height))
                .setContentMargin(Integer.valueOf(margin))
                .setContentColor(color_black)
                .setBackgroundColor(color_white)
                .setLogo(logoBitmap)
                .setContentFillImg(blackBitmap)
                .build();
        iv_qrcode.setImageBitmap(qrcode_bitmap);
    }

    /**
     * 弹出选择框（拍照或从相册选取图片）
     *
     * @author xch
     */
    private void showChooseDialog() {
        AlertDialog.Builder choiceBuilder = new AlertDialog.Builder(this);
        choiceBuilder.setCancelable(false);
        choiceBuilder
                .setTitle("选择图片")
                .setSingleChoiceItems(new String[]{"拍照上传", "从相册选择"}, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0://拍照
                                        takePhoto();
                                        break;
                                    case 1:// 从相册选择
                                        choosePhotoFromAlbum();
                                        break;
                                    default:
                                        break;
                                }
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        choiceBuilder.create();
        choiceBuilder.show();
    }

    /**
     * 长按二维码图片弹出选择框（保存或分享）
     */
    private void imgChooseDialog() {
        AlertDialog.Builder choiceBuilder = new AlertDialog.Builder(MainActivity.this);
        choiceBuilder.setCancelable(false);
        choiceBuilder
                .setTitle("选择")
                .setSingleChoiceItems(new String[]{"存储至手机", "分享"}, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0://存储
                                        saveImg(qrcode_bitmap);
                                        break;
                                    case 1:// 分享
                                        shareImg(qrcode_bitmap);
                                        break;
                                    default:
                                        break;
                                }
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        choiceBuilder.create();
        choiceBuilder.show();
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        // 创建File对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.xch.generateqrcode.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    /**
     * 从相册选取图片
     */
    private void choosePhotoFromAlbum() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbum();
        }
    }

    /**
     * /打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请，可能无法打开相册哟", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        if (remark == 0) {//logo
                            logoBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                            // 将拍摄的照片显示出来
                            picture_logo.setImageBitmap(logoBitmap);
                        } else if (remark == 1) {//black
                            blackBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                            picture_black.setImageBitmap(blackBitmap);
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 4.4以后
     *
     * @param data
     */
    @SuppressLint("NewApi")
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    /**
     * 4.4版本以前，直接获取真实路径
     *
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 显示图片
     *
     * @param imagePath 图片路径
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {

            if (remark == 0) {//logo
                logoBitmap = BitmapFactory.decodeFile(imagePath);
                // 显示图片
                picture_logo.setImageBitmap(logoBitmap);
            } else if (remark == 1) {//black
                blackBitmap = BitmapFactory.decodeFile(imagePath);
                picture_black.setImageBitmap(blackBitmap);
            } else {
            }
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }
}
