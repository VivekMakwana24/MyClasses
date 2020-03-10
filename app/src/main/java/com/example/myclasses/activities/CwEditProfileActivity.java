package com.example.myclasses.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.myclasses.R;
import com.example.myclasses.adapter.CountryCodeAdapter;
import com.example.myclasses.databinding.ActivityCwEditProfileBinding;
import com.example.myclasses.fileUtils.CommonUtils;
import com.example.myclasses.fileUtils.FileUtil;
import com.example.myclasses.model.CountryCodePojoItem;
import com.google.android.material.snackbar.Snackbar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.theartofdev.edmodo.cropper.CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE;

public class CwEditProfileActivity extends AppCompatActivity implements CountryCodeAdapter.countryCodeInterface {

    ActivityCwEditProfileBinding binding;

    private Context context;

    /* Image picker variable */
    private int aspectRationX = 1;
    private int aspectRationY = 1;
    private Uri mCropImageUri;
    private boolean isImageFile = false;
    private String imagePath = "";

    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 111;
    private static final int REQ_OPEN_EXPLORER = 121;
    private static final int REQ_SECURITY_PAPER = 122;
    private static final int REQ_MILITARY_STATUS = 123;

    private String realPathPhotoId = "", realPathSecurityPaper = "", realPathMilitaryStatus = "", fileName = "";
    private boolean attachedFile = false;
    private Uri uri;

    private AsyncTask task;

    /*Country Code*/
    ArrayList<CountryCodePojoItem> arrayListCountryCode = new ArrayList<>();
    private CountryCodeAdapter adapterCountry;
    private Dialog dialogCountry;
//    private DialogServiceTypeBinding bindingCounrty;


    private String countryId = "", countryCode = "", countryName = "", userContact = "";

    private String verificationId = "";
    private int verificationCode;

    private boolean isOTP = false;


    private static final String TAG = "CarWasherEditProfile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_cw_edit_profile);

        initViews();

        setUpToolbar();

        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            binding.edtFirstName.setText(intent.getStringExtra("userFname"));
            binding.edtLastName.setText(intent.getStringExtra("userLname"));
            userContact = intent.getStringExtra("userContact");
            binding.edtContact.setText(userContact);
            binding.edtEmail.setText(intent.getStringExtra("userEmail"));
            binding.edtAge.setText(intent.getStringExtra("userAge"));
            binding.edtAddress.setText(intent.getStringExtra("userAddress"));
            binding.edtEducationLevel.setText(intent.getStringExtra("userEducation"));
            binding.edtExperienceDesc.setText(intent.getStringExtra("userExpDesc"));
            binding.txtUploadPhotoID.setText(intent.getStringExtra("userPhotoId"));
            binding.txtSecurityPaper.setText(intent.getStringExtra("userSecurityPaper"));
            binding.txtMilitaryStatus.setText(intent.getStringExtra("userMilitaryStatus"));
            binding.edtVehicleNumber.setText(intent.getStringExtra("userVehicleNum"));

            Glide.with(context).load(intent.getStringExtra("userProfileImage")).into(binding.ivProfile);
            countryCode = intent.getStringExtra("userCountryCode");
        }

        getCountryCode();

        buttonClicks();

        binding.layoutOTP.setVisibility(View.GONE);
    }


    private void initViews() {
        context = this;

    }


    private void buttonClicks() {

        binding.txtCountryCode.setOnClickListener(view -> {
            binding.txtCountryCode.setClickable(false);

          /*  LayoutInflater layoutInflater = LayoutInflater.from(context);
            bindingCounrty = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_service_type, null, false);
            dialogCountry = new Dialog(context);
            dialogCountry.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogCountry.setCancelable(false);
            dialogCountry.setContentView(bindingCounrty.getRoot());
            dialogCountry.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialogCountry.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            initDialog();

            dialogCountry.show();

            bindingCounrty.txtHeader.setText(context.getString(R.string.country_code_full));

            bindingCounrty.ivClose.setOnClickListener(view1 -> {
                binding.txtCountryCode.setClickable(true);

                dialogCountry.dismiss();
            });*/
        });

        binding.btnSendOTP.setOnClickListener(view -> {
/*            if (binding.edtContact.getText().toString().length() < 10)
                Toast.makeText(context, context.getString(R.string.str_enter_valid_contact), Toast.LENGTH_SHORT).show();
            else
                otpServiceCall();*/
        });

        binding.btnUpdate.setOnClickListener(view -> {
            if (validateForm()) {
                Log.e(TAG, "onClick: ");
                editProfileServiceCall();

            }
        });

        binding.btnCancel.setOnClickListener(view -> onBackPressed());


        binding.ivUploadPhotoId.setOnClickListener(view -> checkPermissionAndUploadDocument(REQ_OPEN_EXPLORER));

        binding.ivSecurityPaper.setOnClickListener(view -> checkPermissionAndUploadDocument(REQ_SECURITY_PAPER));

        binding.ivMilitaryStatus.setOnClickListener(view -> checkPermissionAndUploadDocument(REQ_MILITARY_STATUS));

        binding.ivCamera.setOnClickListener(view -> {
            if (CropImage.isExplicitCameraPermissionRequired(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                }
            } else {
                //CropImage.startPickImageActivity(getActivity());
                startActivityForResult(CropImage.getPickImageChooserIntent(context), PICK_IMAGE_CHOOSER_REQUEST_CODE);
            }
        });

    }

    private void initDialog() {
       /* bindingCounrty.rvServiceType.setLayoutManager(new LinearLayoutManager(context));
        adapterCountry = new CountryCodeAdapter(context, arrayListCountryCode, this);
        bindingCounrty.rvServiceType.setAdapter(adapterCountry);
        adapterCountry.notifyDataSetChanged();*/
    }


    private void changeProfilePicture(String imagePath) {
        /*binding.ivCameraProgress.setVisibility(View.VISIBLE);
        binding.ivCamera.setVisibility(View.GONE);
        String url = ServiceUrls.BASE_URL + ServiceUrls.PROFILE;

        LinkedHashMap<String, String> request = new LinkedHashMap<>();
        LinkedHashMap<String, File> fileRequest = new LinkedHashMap<>();

        request.put(ServiceUrls.ACTION, "change_profile_picture");
        request.put(ServiceUrls.LANG_ID, PrefsUtil.with(context).readString("langId"));
        request.put(ServiceUrls.USER_ID, PrefsUtil.with(context).readString("userId"));

        fileRequest.put(ServiceUrls.PROFILE_PICTURE, new File(imagePath));


        new WebServiceCall(context, url, request, fileRequest, ProfilePicPojo.class, false, new WebServiceCall.OnResultListener() {
            @Override
            public void onResult(boolean status, Object obj) {

                if (status) {

                    ProfilePicPojo result = (ProfilePicPojo) obj;
                    binding.ivCameraProgress.setVisibility(View.GONE);
                    binding.ivCamera.setVisibility(View.VISIBLE);

                    setResult(RESULT_OK);

                    Toast.makeText(context, "" + result.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    binding.ivCameraProgress.setVisibility(View.GONE);
                    binding.ivCamera.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAsync(AsyncTask asyncTask) {
                task = asyncTask;
            }

            @Override
            public void onCancelled() {
                task = null;
            }
        });
*/
    }

    private void editProfileServiceCall() {
/*

        binding.progressBtnLayout.setVisibility(View.VISIBLE);
        binding.btnUpdate.setVisibility(View.GONE);

        String url = ServiceUrls.BASE_URL + ServiceUrls.PROFILE;

        LinkedHashMap<String, String> request = new LinkedHashMap<>();
        LinkedHashMap<String, File> fileRequest = new LinkedHashMap<>();

        request.put(ServiceUrls.ACTION, "edit_profile_washer");
        request.put(ServiceUrls.LANG_ID, PrefsUtil.with(context).readString("langId"));
        request.put(ServiceUrls.USER_ID, PrefsUtil.with(context).readString("userId"));
        request.put(ServiceUrls.SIGNUP_FIRSTNAME, binding.edtFirstName.getText().toString().trim());
        request.put(ServiceUrls.SIGNUP_LASTNAME, binding.edtLastName.getText().toString().trim());
        request.put(ServiceUrls.SIGNUP_EMAIL, binding.edtEmail.getText().toString().trim());
        request.put(ServiceUrls.SIGNUP_COUNTRY_CODE, countryCode);
        request.put(ServiceUrls.SIGNUP_CONTACT, binding.edtContact.getText().toString().trim());
        request.put(ServiceUrls.SIGNUP_AGE, binding.edtAge.getText().toString().trim());
        request.put(ServiceUrls.SIGNUP_ADDRESS, binding.edtAddress.getText().toString().trim());
        request.put(ServiceUrls.SIGNUP_EDUCATION_LEVEL, binding.edtEducationLevel.getText().toString().trim());
        request.put(ServiceUrls.SIGNUP_EXP_DESC, binding.edtExperienceDesc.getText().toString().trim());
        request.put(ServiceUrls.SIGNUP_VEHICLE_NO, binding.edtVehicleNumber.getText().toString().trim());
        request.put(ServiceUrls.SIGNUP_VERIFICATION_ID, verificationId);
        request.put(ServiceUrls.SIGNUP_VERIFICATION_CODE, verificationCode == 0 ? "" : binding.edtOtp.getText().toString().trim());

        if (realPathPhotoId.equalsIgnoreCase("")) {
            request.put(ServiceUrls.SIGNUP_PHOTO_ID, binding.txtUploadPhotoID.getText().toString().trim());
        } else {
            fileRequest.put(ServiceUrls.SIGNUP_PHOTO_ID, new File(realPathPhotoId));
        }

        if (realPathSecurityPaper.equalsIgnoreCase("")) {
            request.put(ServiceUrls.SIGNUP_SECURITY_PAPER, binding.txtSecurityPaper.getText().toString().trim());
        } else {
            fileRequest.put(ServiceUrls.SIGNUP_SECURITY_PAPER, new File(realPathSecurityPaper));
        }

        if (realPathMilitaryStatus.equalsIgnoreCase("")) {
            request.put(ServiceUrls.SIGNUP_MILITARY_STATUS, binding.txtMilitaryStatus.getText().toString().trim());

        } else {
            fileRequest.put(ServiceUrls.SIGNUP_MILITARY_STATUS, new File(realPathMilitaryStatus));
        }

        new WebServiceCall(context, url, request, fileRequest, CommonPojo.class, false
                , new WebServiceCall.OnResultListener() {
            @Override
            public void onResult(boolean status, Object obj) {

                if (status) {
                    binding.progressBtnLayout.setVisibility(View.GONE);
                    binding.btnUpdate.setVisibility(View.VISIBLE);
                    CommonPojo result = (CommonPojo) obj;

                    Log.e(TAG, "onResult: ");

                    PrefsUtil.with(context).write("userFname", binding.edtFirstName.getText().toString().trim());
                    PrefsUtil.with(context).write("userLname", binding.edtLastName.getText().toString().trim());
                    PrefsUtil.with(context).write("userContact", binding.edtContact.getText().toString().trim());
                    PrefsUtil.with(context).write("userEmail", binding.edtEmail.getText().toString().trim());


                    Toast.makeText(context, "" + result.getMessage(), Toast.LENGTH_LONG).show();

                    setResult(RESULT_OK);
                    finish();


                } else {
                    binding.progressBtnLayout.setVisibility(View.GONE);
                    binding.btnUpdate.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "" + obj, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onAsync(AsyncTask asyncTask) {
                task = asyncTask;
            }

            @Override
            public void onCancelled() {
                task = null;
            }
        });
*/


    }

    private boolean validateForm() {
        boolean result = true;
        binding.edtFirstName.setError(null);
        binding.edtLastName.setError(null);
        binding.edtContact.setError(null);
        binding.edtOtp.setError(null);
        binding.edtEmail.setError(null);
        binding.edtVehicleNumber.setError(null);
        binding.edtExperienceDesc.setError(null);
        binding.edtEducationLevel.setError(null);
        binding.edtAge.setError(null);
        binding.edtAddress.setError(null);

//        if (binding.edtFirstName.getText().toString().trim().isEmpty()) {
//            binding.edtFirstName.setError(getString(R.string.first_name_req));
//            result = false;
//        }
//        if (binding.edtLastName.getText().toString().trim().isEmpty()) {
//            binding.edtLastName.setError(context.getString(R.string.last_name_req));
//            result = false;
//        }
//
//        if (binding.edtContact.getText().toString().trim().isEmpty()) {
//            binding.edtContact.setError(context.getString(R.string.contact_req));
//            result = false;
//        }
//
//        if (binding.edtEmail.getText().toString().trim().isEmpty()) {
//            binding.edtEmail.setError(context.getString(R.string.email_req));
//            binding.edtEmail.requestFocus();
//            result = false;
//        }
//
//        if (binding.edtAge.getText().toString().trim().isEmpty()) {
//            binding.edtAge.setError(context.getString(R.string.age_req));
//            result = false;
//        }
//
//        if (binding.edtEducationLevel.getText().toString().trim().isEmpty()) {
//            binding.edtEducationLevel.setError(context.getString(R.string.education_level_req));
//            result = false;
//        }
//
//        if (binding.edtAddress.getText().toString().trim().isEmpty()) {
//            binding.edtAddress.setError(context.getString(R.string.address_req));
//            result = false;
//        }
//
//        if (binding.edtExperienceDesc.getText().toString().trim().isEmpty()) {
//            binding.edtExperienceDesc.setError(context.getString(R.string.education_desc_req));
//            result = false;
//        }
//
//        if (binding.edtVehicleNumber.getText().toString().trim().isEmpty()) {
//            binding.edtVehicleNumber.setError(context.getString(R.string.vehicle_number_req));
//            result = false;
//        }
//
//        if (binding.txtUploadPhotoID.getText().toString().matches("")) {
//            Toast.makeText(context, context.getString(R.string.please_upload_photo_id), Toast.LENGTH_SHORT).show();
//            result = false;
//        }
//
//        if (binding.txtSecurityPaper.getText().toString().matches("")) {
//            Toast.makeText(context, context.getString(R.string.please_upload_security_paper), Toast.LENGTH_SHORT).show();
//            result = false;
//        }
//
//        if (binding.txtMilitaryStatus.getText().toString().matches("")) {
//            Toast.makeText(context, context.getString(R.string.please_upload_military_status), Toast.LENGTH_SHORT).show();
//            result = false;
//        }
//
//        if (countryCode.equalsIgnoreCase("")) {
//            result = false;
//            binding.txtCountryCode.requestFocus();
//            Toast.makeText(context, "" + context.getString(R.string.country_code_req), Toast.LENGTH_SHORT).show();
//        }
//
//        if (isOTP) {
//            if (binding.edtOtp.getText().toString().trim().isEmpty()) {
//                binding.edtOtp.setError(context.getString(R.string.otp_req));
//                binding.edtOtp.requestFocus();
//                result = false;
//            }
//        }
//
//        if (result) {
//            if (binding.edtFirstName.getText().toString().length() < 3) {
//                binding.edtFirstName.setError(getString(R.string.str_valid_firstname));
//                binding.edtFirstName.requestFocus();
//                result = false;
//            } else if (binding.edtLastName.getText().toString().length() < 3) {
//                binding.edtLastName.setError(getString(R.string.str_valid_lastname));
//                binding.edtLastName.requestFocus();
//                result = false;
//            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.edtEmail.getText().toString().trim()).matches()) {
//                binding.edtEmail.setError(getString(R.string.str_valid_email));
//                binding.edtEmail.requestFocus();
//                result = false;
//            } else if ((binding.edtContact.getText().toString().length() < 10) ||
//                    binding.edtContact.getText().toString().length() >= 15) {
//                binding.edtContact.setError(getString(R.string.str_valid_contact));
//                binding.edtContact.requestFocus();
//                result = false;
//            } else if (!TextUtils.isDigitsOnly(binding.edtAge.getText().toString())) {
//                binding.edtAge.setError(getString(R.string.str_valid_age));
//                binding.edtAge.requestFocus();
//                result = false;
//            } else if (!(binding.edtContact.getText().toString().equalsIgnoreCase(userContact))) {
//                Log.e(TAG, "validateForm: ");
//                if (isOTP) {
//                    Log.e(TAG, "ISOTP: " + isOTP);
//                    if (!binding.edtOtp.getText().toString().trim().equalsIgnoreCase(String.valueOf(verificationCode))) {
//                        binding.edtOtp.setError(context.getString(R.string.otp_incorrect));
//                        binding.edtOtp.requestFocus();
//                        result = false;
//                    }
//                } else {
//                    Log.e(TAG, "validateForm: Error");
//                    result = false;
//                    Snackbar.make(binding.btnSendOtpLayout, R.string.contact_changed, Snackbar.LENGTH_LONG).show();
//                }
//            }
//        }
        Log.e(TAG, "validateForm Result :: " + result);
        return result;
    }

    private void getCountryCode() {
/*

        String url = ServiceUrls.BASE_URL + ServiceUrls.PROFILE;
        binding.txtCountryCode.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);

        try {
            LinkedHashMap<String, String> request = new LinkedHashMap<>();

            request.put(ServiceUrls.ACTION, ServiceUrls.COUNTRY_CODE_LIST);

            new WebServiceCall(context, url, request, CountryCodePojo.class, false, new WebServiceCall.OnResultListener() {
                @Override
                public void onResult(boolean status, Object obj) {
                    if (status) {
                        CountryCodePojo result = (CountryCodePojo) obj;
                        binding.progressBar.setVisibility(View.GONE);
                        binding.txtCountryCode.setVisibility(View.VISIBLE);
                        arrayListCountryCode.clear();

                        arrayListCountryCode.addAll(result.getCountryCodePojo());

                        for (int i = 0; i < arrayListCountryCode.size(); i++) {
                            if (arrayListCountryCode.get(i).getCountryCode().equalsIgnoreCase(countryCode)) {
                                binding.txtCountryCode.setText(arrayListCountryCode.get(i).getCountryCode());
                            }
                        }
//                        adapterCountryCode.notifyDataSetChanged();

                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.txtCountryCode.setVisibility(View.VISIBLE);

                        Toast.makeText(context, obj.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onAsync(AsyncTask asyncTask) {
                    task = asyncTask;
                }

                @Override
                public void onCancelled() {
                    task = null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
*/

    }

    private void otpServiceCall() {
       /* binding.btnSendOTP.setVisibility(View.GONE);
        binding.progressBarSendOtp.setVisibility(View.VISIBLE);

        String url = ServiceUrls.BASE_URL + ServiceUrls.PROFILE;
        try {
            LinkedHashMap<String, String> request = new LinkedHashMap<>();

            request.put(ServiceUrls.ACTION, ServiceUrls.SIGNUP_OTP);
            request.put(ServiceUrls.SIGNUP_CONTACT, countryCode + binding.edtContact.getText().toString().trim());

            new WebServiceCall(context, url, request, GetOTPPojo.class, false, new WebServiceCall.OnResultListener() {
                @Override
                public void onResult(boolean status, Object obj) {
                    if (status) {
                        binding.progressBarSendOtp.setVisibility(View.GONE);
                        binding.btnSendOTP.setVisibility(View.VISIBLE);

                        GetOTPPojo result = (GetOTPPojo) obj;

                        verificationId = result.getData().getVerificationId();
                        verificationCode = result.getData().getVerificationCode();

                        DialogClass dialogClass = new DialogClass(context, null,
                                result.getMessage());
                        dialogClass.showDialog().show();
                        isOTP = true;
                        binding.layoutOTP.setVisibility(View.VISIBLE);
                    } else {
                        binding.progressBarSendOtp.setVisibility(View.GONE);
                        binding.btnSendOTP.setVisibility(View.VISIBLE);
                        Toast.makeText(context, "" + obj, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onAsync(AsyncTask asyncTask) {
                    task = asyncTask;
                }

                @Override
                public void onCancelled() {
                    task = null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void setUpToolbar() {
        /*binding.toolbar.txtTitle.setText(context.getString(R.string.edit_profile).toUpperCase());
        binding.toolbar.toolbarIcon.setImageResource(R.drawable.nav_back_rtl);
        binding.toolbar.toolbarIcon.setOnClickListener(view -> {
            finish();
            if (isLeftToRight) {
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else {
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });*/
    }

    private void checkPermissionAndUploadDocument(int code) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                browseDocuments(code);

            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            browseDocuments(code);
        }
    }

    private void browseDocuments(int reqCode) {
        Intent intent = new Intent()
                .setAction(Intent.ACTION_OPEN_DOCUMENT);
//        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        String[] mimetypes = {"application/pdf", "image/*"};
//        String[] mimetypes = {mimeType};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        startActivityForResult(intent, reqCode);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_OPEN_EXPLORER) {
                if (data != null) {
                    //realPath = getRealPath(data.getData());
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        // Call some material design APIs here
                        realPathPhotoId = CommonUtils.getPath(context, data.getData());
                        Log.e(TAG, "PhotoId" + realPathPhotoId);
                        fileName = realPathPhotoId.substring(realPathPhotoId.lastIndexOf("/") + 1);
                        attachedFile = true;
                        binding.txtUploadPhotoID.setText(fileName);
                    } else {
                        // Implement this feature without material design
                        uri = data.getData();
                        Log.e(TAG, "PhotoId onActivityResult: " + uri);
//                        fileName = CommonUtils.stripExtension(CommonUtils.getFileName(context, uri));
                        fileName = FileUtil.getFileName(context, uri);
                        FileUtil.filePath = FileUtil.getPath(context, uri, result -> {
                            attachedFile = true;
                            realPathPhotoId = result;
                            Log.e(TAG, "PhotoId Path" + realPathPhotoId);
                            binding.txtUploadPhotoID.setText(fileName);
                            return null;
                        });
                    }
                }
            } else if (requestCode == REQ_SECURITY_PAPER) {
                if (data != null) {
                    //realPath = getRealPath(data.getData());
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        // Call some material design APIs here
                        realPathSecurityPaper = CommonUtils.getPath(context, data.getData());
                        Log.e(TAG, "securityPaper" + realPathSecurityPaper);
                        fileName = realPathSecurityPaper.substring(realPathSecurityPaper.lastIndexOf("/") + 1);
                        attachedFile = true;
                        binding.txtSecurityPaper.setText(fileName);
                    } else {
                        // Implement this feature without material design
                        uri = data.getData();
                        Log.e(TAG, "securityPaper onActivityResult: " + uri);
//                        fileName = CommonUtils.stripExtension(CommonUtils.getFileName(context, uri));
                        fileName = FileUtil.getFileName(context, uri);
                        FileUtil.filePath = FileUtil.getPath(context, uri, result -> {
                            attachedFile = true;
                            realPathSecurityPaper = result;
                            Log.e(TAG, "securityPaper PATH" + realPathSecurityPaper);
                            binding.txtSecurityPaper.setText(fileName);
                            return null;
                        });
                    }
                }
            } else if (requestCode == REQ_MILITARY_STATUS) {
                if (data != null) {
                    //realPath = getRealPath(data.getData());
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        // Call some material design APIs here
                        realPathMilitaryStatus = CommonUtils.getPath(context, data.getData());
                        Log.e(TAG, "MilitaryStatus" + realPathMilitaryStatus);
                        fileName = realPathMilitaryStatus.substring(realPathMilitaryStatus.lastIndexOf("/") + 1);
                        attachedFile = true;
                        binding.txtMilitaryStatus.setText(fileName);
                    } else {
                        // Implement this feature without material design
                        uri = data.getData();
                        Log.e(TAG, "MilitaryStatus onActivityResult: " + uri);
//                        fileName = CommonUtils.stripExtension(CommonUtils.getFileName(context, uri));
                        fileName = FileUtil.getFileName(context, uri);
                        FileUtil.filePath = FileUtil.getPath(context, uri, result -> {
                            attachedFile = true;
                            realPathMilitaryStatus = result;
                            Log.e(TAG, "MilitaryStatus PATH" + realPathMilitaryStatus);
                            binding.txtMilitaryStatus.setText(fileName);
                            return null;
                        });
                    }
                }

            }
        }
        if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(context, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (CropImage.isReadExternalStoragePermissionsRequired(context, imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                /*mCurrentFragment.setImageUri(imageUri);*/
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(aspectRationX, aspectRationY)
                        .start(this);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.e("RESULT URI IS : ", result.getUri().getPath());
                imagePath = result.getUri().getPath();

                Glide.with(context).load(result.getUri().getPath()).into(binding.ivProfile);

                changeProfilePicture(imagePath);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
                browseDocuments(REQ_OPEN_EXPLORER);
            }
        }

        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CropImage.startPickImageActivity(this);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(((Activity) context), Manifest.permission.CAMERA)) {
                    Toast.makeText(context, "Permission Error", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                }
                Toast.makeText(context, "Cancel", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*mCurrentFragment.setImageUri(mCropImageUri);*/
                CropImage.activity(mCropImageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(aspectRationX, aspectRationY)
                        .start(this);
            } else {
                Toast.makeText(context, "Cancel Permission", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void codeClick(int position) {
        if (dialogCountry.isShowing()) {
            dialogCountry.dismiss();
        }
        binding.txtCountryCode.setClickable(true);

        binding.txtCountryCode.setText(arrayListCountryCode.get(position).getCountryCode());

        countryCode = arrayListCountryCode.get(position).getCountryCode();
        countryId = arrayListCountryCode.get(position).getId();
        countryName = arrayListCountryCode.get(position).getCountryName();
        Log.e(TAG, "countryCode: " + countryCode);
    }

    @Override
    protected void onDestroy() {
        try {
            if (task != null) {
                task.cancel(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
