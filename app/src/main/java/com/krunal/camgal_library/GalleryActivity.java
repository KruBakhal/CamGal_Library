package com.krunal.camgal_library;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.krunal.camgal_library.Adapter.FolderAdapter_Native;
import com.krunal.camgal_library.Adapter.MyPagerAdapter_Native;
import com.krunal.camgal_library.Adapter.SelectedImageAdapter_Native;
import com.krunal.camgal_library.Frag.Gallery_Fragment_Native;
import com.krunal.camgal_library.Intermediate.OnImageListner;
import com.krunal.camgal_library.Intermediate.SelectedAdapterListener;
import com.krunal.camgal_library.Intermediate.onAddItemListner;
import com.krunal.camgal_library.Model.GalleryModel;
import com.krunal.camgal_library.Model.ImageModel;
import com.krunal.camgal_library.Utils.BookStore;
import com.krunal.camgal_library.Utils.Constant;
import com.krunal.camgal_library.Utils.FileUtils;
import com.krunal.camgal_library.Utils.HelperResizer;
import com.krunal.camgal_library.databinding.ActivityGalleryBinding;

import java.io.File;
import java.util.ArrayList;

import static com.krunal.camgal_library.Utils.Utility.ARG_PARAM1;
import static com.krunal.camgal_library.Utils.Utility.displayToast;
import static com.krunal.camgal_library.Utils.Utility.forUriSecurity;
import static com.krunal.camgal_library.Utils.Utility.getGalleryTypeString;
import static com.krunal.camgal_library.Utils.Utility.getList_ByFolderName;
import static com.krunal.camgal_library.Utils.Utility.onClickEvent;

public class GalleryActivity extends AppCompatActivity implements FolderAdapter_Native.FolderAdapterListener,
        SelectedAdapterListener,
        OnImageListner,
        onAddItemListner {
    Context context;
    RecyclerView rv_FolderList;
    RecyclerView rv_selectedImageList;
    LinearLayout layBottom, innerLay, layTop, actionBar, layText;
    View layDone;
    ImageView btnBack, btnDone;
    TextView tvTitle, tv1, tv2;
    ArrayList<GalleryModel> listFolder;
    SelectedImageAdapter_Native imageSelectedAdapter;
    FolderAdapter_Native folderAdapter;
    ViewPager viewPager;
    private ImageView pdLoading;
    private ArrayList<Fragment> list;
    public MyPagerAdapter_Native myPagerAdapter;
    private GalleryList galleryList;
    private View view;
    private FileUtils utilsStorage;
    private @NonNull
    ActivityGalleryBinding viewBinding;
    private BookStore bookStore;
    public String folder_position_native = null;
    public int selectedPosition = 0;
    public ArrayList<ImageModel> list_new_pic_selected = new ArrayList<>();
    public ArrayList<ImageModel> selected_OriginalImageList;
    public int TotalImgCount = 5;
    private int compressionSize = 0;
    private String gallery_type = null;
    private String compression_Type = null;
    private boolean compressStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        view = viewBinding.getRoot();
        context = this;
        new FileUtils().cacheFolder(GalleryActivity.this);
        initData();
        ui();
        resize();
        setAdapter();
        click();
    }

    private void initData() {
        context = this;
        utilsStorage = new FileUtils();
        utilsStorage.cacheFolder(context);
        bookStore = new BookStore(context);

        String g_type = getIntent().getStringExtra(Constant.GALLERY_TYPE);
        if (!TextUtils.isEmpty(g_type)) {
            gallery_type = getGalleryTypeString(Integer.parseInt(g_type));
        }
        if (TextUtils.isEmpty(gallery_type)) {
            gallery_type = getGalleryTypeString(bookStore.get_GALLERY_TYPE());
        }

        TotalImgCount = getIntent().getIntExtra(Constant.IMAGE_Selection_COUNT, bookStore.get_Image_Selection_Count());

        compressStatus = getIntent().getBooleanExtra(Constant.COMPRESS_STATUS, bookStore.get_COMPRESS_STATUS());

        compressionSize = getIntent().getIntExtra(Constant.COMPRESSION_SIZE, bookStore.get_COMPRESSION_SIZE());

        String c_type = getIntent().getStringExtra(Constant.COMPRESSION_SIZE_Type);
        if (!TextUtils.isEmpty(c_type)) {
            if (c_type.equals("MB")) {
                compression_Type = "MB";
            } else if (c_type.equals("KB")) {
                compression_Type = "KB";
            }
        }
        if (TextUtils.isEmpty(compression_Type)) {
            compression_Type = bookStore.get_COMPRESSION_Type();
        }
    }

    private void click() {
        layDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNext(v);
                onClickEvent(v);
            }
        });
        viewBinding.layBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigation.findNavController(viewBinding.getRoot()).popBackStack();
                onBackPressed();
                onClickEvent(v);
            }
        });


        if (viewPager != null) {

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    folder_position_native = "" + position;
                    rv_FolderList.scrollToPosition(position);
                    viewPager.setCurrentItem(position);
                    Gallery_Fragment_Native fragment = (Gallery_Fragment_Native) myPagerAdapter.getItem(viewPager.getCurrentItem());
                    if (fragment != null && fragment.recyclerViewImage != null) {
                        int y = fragment.recyclerViewImage.getScrollY();
                        if (listFolder != null && !listFolder.isEmpty() && !TextUtils.isEmpty(folder_position_native)) {
                            GalleryModel list = listFolder.get(Integer.parseInt(folder_position_native));
                            if (list != null && list.getListImage() != null && !list.getListImage().isEmpty())
                                fragment.notifyListChange(list.getListImage(), y);
                        }
                        fragment.recyclerViewImage.setScrollY(y);
                    }

                    folderAdapter.notifyDataSetChanged();

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

    }

    private void ui() {
        actionBar = view.findViewById(R.id.actionBar);
        layTop = view.findViewById(R.id.layTop);
        layBottom = view.findViewById(R.id.layBottom);
        innerLay = view.findViewById(R.id.innerLay);
        layText = view.findViewById(R.id.layText);
        btnBack = view.findViewById(R.id.btnBack);
        btnDone = view.findViewById(R.id.btnDone);
        tvTitle = view.findViewById(R.id.tv_title);
        layDone = view.findViewById(R.id.layDone);
        tv1 = view.findViewById(R.id.tv1);
        tv2 = view.findViewById(R.id.tv2);
        rv_FolderList = view.findViewById(R.id.recyclerViewFolder);
        rv_selectedImageList = view.findViewById(R.id.rv_selectedImageList);
        viewPager = view.findViewById(R.id.viewPager);
        pdLoading = view.findViewById(R.id.pdLoading);
        tvTitle.setText("Photo Gallery");
        Glide.with(context).load(R.drawable.loader1).into(pdLoading);
    }

    private void resize() {
        HelperResizer.getInstance(context);
        HelperResizer.setSize(btnBack, 70, 70, true);
        HelperResizer.setSize(btnDone, 80, 80, true);


        HelperResizer.setSize(pdLoading, 100, 100, true);
        HelperResizer.setHeight(context, innerLay, 250);
        HelperResizer.setHeight(context, actionBar, 150);
        HelperResizer.setHeight(context, layTop, 290);
        HelperResizer.setHeight(context, layText, 120);


    }

    private void setAdapter() {
        forUriSecurity();
        selectedPosition = 0;

        if (selected_OriginalImageList == null || selected_OriginalImageList.isEmpty()) {
            selected_OriginalImageList = new ArrayList<>();
            if (list_new_pic_selected != null && list_new_pic_selected.size() != 0)
                list_new_pic_selected.clear();
            list_new_pic_selected = new ArrayList<>();
        } else {
            if (list_new_pic_selected != null)
                list_new_pic_selected.clear();
            list_new_pic_selected = new ArrayList<>();
            list_new_pic_selected.addAll(selected_OriginalImageList);
        }

        if (list_new_pic_selected == null || list_new_pic_selected.isEmpty()) {
            list_new_pic_selected = new ArrayList<>();
            for (int i = 0; i < TotalImgCount; i++) {
                list_new_pic_selected.add(new ImageModel());
            }
        } else {
            ArrayList<ImageModel> tempList =
                    getList_ByFolderName(utilsStorage.getTemp_OriginalImage_Path(context));
            if (tempList != null && tempList.size() != 0) {
                if (verify_the_List(list_new_pic_selected, tempList)) {
                    for (int i = list_new_pic_selected.size(); i < TotalImgCount; i++) {
                        list_new_pic_selected.add(new ImageModel());
                    }
                }
            } else {
                list_new_pic_selected = new ArrayList<>();
                // add empty model
                for (int i = 0; i < TotalImgCount; i++) {
                    list_new_pic_selected.add(new ImageModel());
                }
            }
        }
        selectedPosition = getEmptySlotPosition(list_new_pic_selected);

        setBottomText();
        setFolder();
        setSelectedImageList();
        if (selectedPosition >= 4) {
            rv_selectedImageList.scrollToPosition(selectedPosition - 3);
        }
        galleryList = (GalleryList) new GalleryList(context, 1, null).execute();

    }


    private int getEmptySlotPosition(ArrayList<ImageModel> selected_originalImageList) {
        for (int i = 0; i < selected_originalImageList.size(); i++) {
            ImageModel imageModel = selected_originalImageList.get(i);
            if (imageModel != null && TextUtils.isEmpty(imageModel.getfPath())) {
                return i;
            }
        }
        return 0;
    }

    private boolean verify_the_List(ArrayList<ImageModel> selected_originalImageList, ArrayList<ImageModel> tempList) {

        int count = 0;
        for (ImageModel imageModel : selected_originalImageList) {
            for (ImageModel model : tempList) {
                if (!TextUtils.isEmpty(imageModel.getCroppedPath()) &&
                        !TextUtils.isEmpty(model.getfPath())
                        && imageModel.getCroppedPath().equals(model.getfPath())) {
                    count++;
                }
            }
        }
        if (count == selected_originalImageList.size()) {
            return true;
        } else
            return false;
    }


    public void onClickNext(View view) {

        if (list_new_pic_selected != null && list_new_pic_selected.size() != 0) {
            ArrayList<ImageModel> picList1 = new ArrayList<>();
            for (int i = 0; i < list_new_pic_selected.size(); i++) {
                if (!TextUtils.isEmpty(list_new_pic_selected.get(i).getfPath())) {
                    picList1.add(list_new_pic_selected.get(i));
                }
            }
            if (picList1 == null || picList1.size() <= 0) {
                displayToast(context, getResources().getString(R.string.capture_image_first));
                return;
            }

            try {
                if (galleryList != null) {
                    galleryList.cancel(true);
                    galleryList = null;
                }
            } catch (Exception e) {
                Log.d("TAG", "onClickNext: " + e.toString());
                return;
            }
/*

            Bundle bundle = new Bundle();
            bundle.putString(ARG_PARAM1, new Gson().toJson(picList1));
            Navigation.findNavController(viewBinding.getRoot())
                    .navigate(R.id.action_galleryFragment_to_imagesFragment, bundle);
*/

            someActivityResultLauncher.launch(new Intent(context, CompressionResizerActivity.class)
                    .putExtra(ARG_PARAM1, new Gson().toJson(picList1))
                    .putExtra(Constant.COMPRESS_STATUS, compressStatus)
                    .putExtra(Constant.COMPRESSION_SIZE_Type, compression_Type)
                    .putExtra(Constant.COMPRESSION_SIZE, compressionSize)
            );


            return;
        }


    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        ArrayList<String> listSaved = result.getData().getStringArrayListExtra("selectedImages");

                        setResult(RESULT_OK, new Intent().putStringArrayListExtra("selectedImages", listSaved));
                        finish();
                    } else if (result.getResultCode() == Activity.RESULT_FIRST_USER) {

                    }

                }
            });

    @Override
    public void onUserFolderClick(final ImageModel image, int i) {

        folder_position_native = "" + i;
        viewPager.setCurrentItem(i);

    }


    @Override
    public void onRemove_From_Selected_list(ImageModel imageModel, int position) {

        if (!TextUtils.isEmpty(imageModel.getCroppedPath()) && new File(imageModel.getCroppedPath()).exists()) {
            new File(imageModel.getCroppedPath()).delete();
        }
        Gallery_Fragment_Native fragment = (Gallery_Fragment_Native) myPagerAdapter.getItem(viewPager.getCurrentItem());
        list_new_pic_selected.set(position, new ImageModel());
        imageSelectedAdapter.notifyItemChanged(position);
        fragment.gridImageViewAdapter.notifyDataSetChanged();
        setBottomText();
    }

    private void setBottomText() {
        int count = 0;
        if (list_new_pic_selected != null && list_new_pic_selected.size() != 0)
            for (int i = 0; i < list_new_pic_selected.size(); i++) {
                if (list_new_pic_selected.get(i).getfPath() != null && list_new_pic_selected.get(i).getfPath().length() != 0) {
                    count++;
                }
            }

        tv1.setText("Please Choose 1-" + list_new_pic_selected.size() + " Photos");
        tv2.setText("(" + count + "/" + TotalImgCount + ")");
        if (count != 0) {
            viewBinding.btnDone.setSelected(true);
        } else {
            viewBinding.btnDone.setSelected(false);
        }
    }


    private void setSelectedImageList() {
        imageSelectedAdapter = new SelectedImageAdapter_Native(this, list_new_pic_selected, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv_selectedImageList.setLayoutManager(layoutManager);
        rv_selectedImageList.setAdapter(imageSelectedAdapter);
        rv_selectedImageList.scrollToPosition(selectedPosition);
        rv_selectedImageList.setScrollX(rv_selectedImageList.computeHorizontalScrollOffset());
        setBottomText();

    }


    private void setFolder() {

        folderAdapter = new FolderAdapter_Native(this, listFolder);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rv_FolderList.setLayoutManager(layoutManager);
        rv_FolderList.setAdapter(folderAdapter);


        list = new ArrayList<>();
        if (listFolder != null && listFolder.size() != 0)
            for (GalleryModel model : listFolder) {
                list.add(new Gallery_Fragment_Native(context, model, GalleryActivity.this));
            }

        myPagerAdapter = new MyPagerAdapter_Native(getSupportFragmentManager(), this, list);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setEnabled(true);
        viewPager.setOffscreenPageLimit(1);
        folder_position_native = "0";
    }

    public boolean checkSlot_AreFull() {
        if (list_new_pic_selected == null || list_new_pic_selected.size() == 0)
            return false;
        int count = 0;
        for (ImageModel imageModel : list_new_pic_selected) {
            if (imageModel != null && !TextUtils.isEmpty(imageModel.getfPath())) {
                count++;
            }
        }

        if (count == 0 || count < list_new_pic_selected.size()) {
            return false;
        } else
            return true;
    }

    @Override
    public void onImageClick(int position) {
        imageSelectedAdapter.notifyListChange(list_new_pic_selected, selectedPosition - 1);

        if (selectedPosition == list_new_pic_selected.size() - 1) {
            ImageModel imageModel = list_new_pic_selected.get(selectedPosition);
            if (imageModel != null && !TextUtils.isEmpty(imageModel.getfPath()) && !checkSlot_AreFull()) {
                selectedPosition = getEmptySlotPosition(list_new_pic_selected);
            }
        }

        imageSelectedAdapter.notifyDataSetChanged();
        setBottomText();

        rv_selectedImageList.smoothScrollToPosition(selectedPosition);
    }

    @Override
    public void onAdd_Item(int finalCount, ImageModel imageModel) {

        GalleryModel galleryModel = listFolder.get(finalCount);
        ArrayList<ImageModel> tempList = galleryModel.getListImage();
        if (tempList == null)
            tempList = new ArrayList<>();
        tempList.add(imageModel);
        galleryModel.setListImage(tempList);
        listFolder.set(finalCount, galleryModel);
    }

    @Override
    public void onAdd(GalleryModel galleryModel) {
        if (listFolder == null)
            listFolder = new ArrayList<>();
        listFolder.add(galleryModel);
    }

    @Override
    public void onRemove() {
        if (listFolder != null && listFolder.size() != 0)
            listFolder.remove(listFolder.size() - 1);
    }

    @Override
    public void onUpdate(int finalCount, GalleryModel galleryModel) {
        if (listFolder == null)
            listFolder = new ArrayList<>();
        listFolder.set(finalCount, galleryModel);
    }


    public class GalleryList extends AsyncTask<Void, Void, Void> {
        private ImageModel imageModel;
        private int type;
        private Context context;


        public GalleryList(Context context, int type, ImageModel imageModel) {
            this.context = context;
            this.type = type;
            this.imageModel = imageModel;
            gallery_type = getGalleryTypeString(bookStore.get_GALLERY_TYPE());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /*pdLoading = new ProgressDialog(context);
            pdLoading.setMessage("Please wait...");
            pdLoading.setCancelable(false);*/

            pdLoading.setVisibility(View.VISIBLE);


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //   if (pdLoading != null && pdLoading.isShowing()) {

            if (type == 1) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (imageSelectedAdapter != null)
                            imageSelectedAdapter.notifyDataSetChanged();
                    }
                });
            }

            String str = "No Image Found In Your Device";
            switch (bookStore.get_GALLERY_TYPE()) {
                case 1:
                    str = "No Image Found Of PNG Image Type In Your Device";
                    break;
                case 2:
                    str = "No Image Found Of JPG Image Type In Your Device";
                    break;
                case 3:
                    str = "No Image Found Of JPEG Image Type In Your Device";
                    break;
            }
            if (listFolder.get(0).getListImage() == null || listFolder.get(0).getListImage().size() <= 0) {
                displayToast(context, str);
            }
            pdLoading.setVisibility(View.GONE);


        }

        @Override
        protected Void doInBackground(Void... voids) {

            listFolder = new ArrayList<>();
            get_All_Folder_Images(context);


            return null;
        }

        public ArrayList<ImageModel> get_Image_BucketsList(Context mContext, String id,
                                                           final ImageModel model) {
            ArrayList<ImageModel> buckets = new ArrayList<>();
            ArrayList<String> blist = new ArrayList<>();

            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            String selection = MediaStore.Images.Media.BUCKET_ID + "=?";
            String[] selectionArgs;
            if (!TextUtils.isEmpty(gallery_type)) {
                selection = selection + " AND " + MediaStore.Images.Media.MIME_TYPE + "=?";
                selectionArgs = new String[]{id, gallery_type};
            } else {
                selectionArgs = new String[]{id};
            }


            String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

            Cursor cursor = mContext.getContentResolver().query(uri, projection, selection, selectionArgs, orderBy);
            boolean isFirst = true;
            if (cursor != null && cursor.getCount() > 0) {
                File file;

                while (cursor.moveToNext()) {
                    String fName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    String fPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    file = new File(fPath);

                    if (file.exists() && !blist.contains(fPath) && !file.getAbsolutePath().endsWith(".gif")) {

                        if (isFirst) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    folderAdapter.addItem(new GalleryModel(model, new ArrayList<ImageModel>()));
                                    myPagerAdapter.addScreen(new Gallery_Fragment_Native(context,
                                            new GalleryModel(model, new ArrayList<ImageModel>()), GalleryActivity.this));

                                }
                            });
                            isFirst = false;
                        }

                        final ImageModel imageModel = new ImageModel(id, fName, fPath);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GalleryModel galleryModel1 = listFolder.get(0);
                                ArrayList<ImageModel> templList = galleryModel1.getListImage();
                                templList.add(imageModel);
                                galleryModel1.setListImage(templList);
                                listFolder.set(0, galleryModel1);

                                GalleryModel galleryModel2 = listFolder.get(listFolder.size() - 1);
                                ArrayList<ImageModel> templList2 = galleryModel2.getListImage();
                                templList2.add(imageModel);
                                galleryModel2.setListImage(templList2);

                                listFolder.set(listFolder.size() - 1, galleryModel2);

                                if (viewPager.getCurrentItem() == 0) {
                                    Gallery_Fragment_Native fragment = (Gallery_Fragment_Native) myPagerAdapter.getItem(0);
                                    if (fragment != null) {
                                        fragment.gridList = galleryModel1.getListImage();
                                        if (fragment.gridImageViewAdapter != null) {
                                            fragment.gridImageViewAdapter.addItem(imageModel);
                                        }
                                    }
                                }
                            }
                        });
                        buckets.add(new ImageModel(id, fName, fPath));
                        blist.add(fPath);
                    }
                }

                cursor.close();
            }
            return buckets;
        }

        public ArrayList<GalleryModel> get_All_Folder_Images(Context mContext) {

            ArrayList<GalleryModel> all_Folder = new ArrayList<>();
            ArrayList<String> blist = new ArrayList<>();

            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.Media.BUCKET_ID,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_MODIFIED,
                    MediaStore.Images.Media.SIZE};
            String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";
            Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, orderBy);

            if (cursor != null) {
                File file = null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        folderAdapter.addItem(new GalleryModel(new ImageModel("", "All", "", ""),
                                new ArrayList<ImageModel>()));
                        myPagerAdapter.addScreen(new Gallery_Fragment_Native(context,
                                new GalleryModel(new ImageModel("", "All", "", ""),
                                        new ArrayList<ImageModel>()), GalleryActivity.this));
                    }
                });
                while (cursor.moveToNext()) {
                    String fName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                    String fPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String fId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
                    String fSize = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                    String fModified = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));


                    file = new File(fPath);

                    if (file.exists() && !blist.contains(fId)) {

                        final ArrayList<ImageModel> listImage;
                        listImage = get_Image_BucketsList(mContext, fId, new ImageModel(fId, fName, fPath));
                        final GalleryModel galleryModel =
                                new GalleryModel(new ImageModel
                                        (fId, fName, fPath), listImage);

                        if (listImage != null && listImage.size() > 0) {
                            all_Folder.add(galleryModel);
                            blist.add(fId);
                        }


                    }


                }
                cursor.close();
            }

            return all_Folder;
        }


    }


    @Override
    public void onDestroy() {
        if (galleryList != null) {
            galleryList.cancel(true);
            galleryList = null;
        }
        super.onDestroy();
        folder_position_native = null;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}