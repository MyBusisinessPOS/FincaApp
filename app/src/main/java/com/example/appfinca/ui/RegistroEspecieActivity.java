package com.example.appfinca.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.appfinca.Actividades;
import com.example.appfinca.BuildConfig;
import com.example.appfinca.R;
import com.example.appfinca.db.bean.InventarioBean;
import com.example.appfinca.db.bean.LoteBean;
import com.example.appfinca.db.dao.InventarioDao;
import com.example.appfinca.db.dao.LoteDao;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegistroEspecieActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int PERMISSION_REQUEST_CODE = 3;
    private Uri imageUri = null;

    private String imagePath = "NO";
    private AutoCompleteTextView mAutoCompleteTextView;
    private AutoCompleteTextView mAutoCompleteTextViewSexo;
    private MaterialButton btnNuevoLote;
    private List<LoteBean> listadoLotes;
    private ImageView img_ejemplar;
    private EditText edit_fecha_nacimiento, et_nombre_animal, et_numero_animal, et_raza_animal;
    LoteBean selectedLote = null;
    private MaterialButton btnGuardar;

    private String selectedSexo = "NO";
    private TextInputLayout outlinedTextFieldNombre, outlinedTextFieldRaza, outlinedTextFieldNumero;
    private String animalGlobal;
    private InventarioBean inventarioBeanEdit;
    private Boolean isUpdate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_especie);

        initControls();
    }

    private void initData(){
        Intent intent = this.getIntent();
        this.animalGlobal = intent.getStringExtra(Actividades.PARAM_1);

        if (animalGlobal.compareToIgnoreCase("CREATE") != 0){
            isUpdate = true;
            final InventarioDao inventarioDao = new InventarioDao();
            inventarioBeanEdit = inventarioDao.getByCodigoAnimal(animalGlobal);
            et_raza_animal.setText(""+ inventarioBeanEdit.getRaza());
            et_numero_animal.setText(""+ inventarioBeanEdit.getCodigo_animal());
            //et_numero_animal.setEnabled(true);
            et_nombre_animal.setText(""+ inventarioBeanEdit.getNombre());
            String imagePath = inventarioBeanEdit.getPath_imagen();
            Uri imageUri = Uri.fromFile(new File(imagePath));
            img_ejemplar.setImageURI(imageUri);
            mAutoCompleteTextViewSexo.setText(""+inventarioBeanEdit.getSexo());
            mAutoCompleteTextView.setText("" + inventarioBeanEdit.getLOTE());
            btnGuardar.setText("ACTUALIZAR");
        }

    }

    private void initControls() {


        outlinedTextFieldNombre = findViewById(R.id.outlinedTextFieldNombre);
        outlinedTextFieldRaza = findViewById(R.id.outlinedTextFieldRaza);
        outlinedTextFieldNumero = findViewById(R.id.outlinedTextFieldNumero);

        et_raza_animal = findViewById(R.id.et_raza_animal);
        et_numero_animal = findViewById(R.id.et_numero_animal);
        et_nombre_animal = findViewById(R.id.et_nombre_animal);

        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarAnimal();
            }
        });
        img_ejemplar = findViewById(R.id.img_ejemplar);
        img_ejemplar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialog();
            }
        });
        mAutoCompleteTextView = findViewById(R.id.dropdownLote);
        mAutoCompleteTextViewSexo = findViewById(R.id.dropdownSexo);
        edit_fecha_nacimiento = findViewById(R.id.edit_fecha_nacimiento);
        edit_fecha_nacimiento.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Calendar.getInstance().getTime()));
        edit_fecha_nacimiento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                Calendar min = Calendar.getInstance();
                min.add(Calendar.DAY_OF_YEAR, -2);
                DatePickerDialog dialog = new DatePickerDialog(RegistroEspecieActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        Calendar now = Calendar.getInstance();
                        now.set(Calendar.YEAR, year);
                        now.set(Calendar.MONTH, monthOfYear);
                        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        ((TextInputLayout) findViewById(R.id.layout_date)).setError(null);
                        edit_fecha_nacimiento.setText(sdf.format(now.getTime()));
                    }

                },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMaxDate(now.getTimeInMillis());
                dialog.show();
            }
        });

        btnNuevoLote = findViewById(R.id.btnNuevoLote);
        btnNuevoLote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoDialogLotes();
            }
        });

        cargaLotes();
        loadSexo();
        initData();
    }

    private void loadSexo() {
        String[] data = new String[]{"Hembra", "Macho"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, data);
        mAutoCompleteTextViewSexo = findViewById(R.id.dropdownSexo);
        mAutoCompleteTextViewSexo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedSexo = (String) parent.getItemAtPosition(position);
            }
        });
        mAutoCompleteTextViewSexo.setAdapter(adapter);
    }


    private void cargaLotes() {
        listadoLotes = (List<LoteBean>) (List<?>) new LoteDao().list();
        loadAutomcompleteLotes();
    }

    private void loadAutomcompleteLotes() {
        ArrayAdapter<LoteBean> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listadoLotes);
        mAutoCompleteTextView = findViewById(R.id.dropdownLote);
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLote = (LoteBean) parent.getItemAtPosition(position);

            }
        });
        mAutoCompleteTextView.setAdapter(adapter);
    }


    private void showImageDialog() {
        Dialog dialog = new Dialog(RegistroEspecieActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_show_camera_galery);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            ImageView cameraImage = dialog.findViewById(R.id.camera_image);
            ImageView galleryImage = dialog.findViewById(R.id.gallery_image);

            cameraImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchPickImageIntent();
                    dialog.dismiss();
                }
            });

            galleryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }


    private void shoDialogLotes() {
        Dialog dialog = new Dialog(RegistroEspecieActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_agregar_lote);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        if (dialog.getWindow() != null) {
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            EditText amountEditText = dialog.findViewById(R.id.edit_amount);
            MaterialButton positiveButton = dialog.findViewById(R.id.okButton);
            MaterialButton negativeButton = dialog.findViewById(R.id.cancelButton);

            positiveButton.setEnabled(false);
            // Colocar el foco en el EditText y abrir el teclado
            amountEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            amountEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String enteredValue = s.toString();
                    if (enteredValue == null || enteredValue.isEmpty()) {
                        amountEditText.setError("Ingrese un Lote");
                        positiveButton.setEnabled(false);
                    } else {
                        amountEditText.setError(null);
                        positiveButton.setEnabled(true);
                    }
                }
            });

            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String category = amountEditText.getText().toString();
                    imm.hideSoftInputFromWindow(amountEditText.getWindowToken(), 0);

                    agregarLote(category);
                    dialog.cancel();
                }
            });
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }
    }


    private void agregarLote(String descripcion) {
        final LoteDao loteDao = new LoteDao();
        final LoteBean loteBean = new LoteBean();
        loteBean.setCategoria(descripcion);
        loteDao.insert(loteBean);
        cargaLotes();
    }


    private void dispatchTakePictureIntent() {

        // Comprobar si los permisos necesarios están concedidos
        if (checkPermission()) {
            // Crear un archivo para almacenar la imagen tomada
            File photoFile = createImageFile();

            // Crear el intent para la cámara
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Asegurarse de que hay una aplicación de cámara disponible para manejar el intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null && photoFile != null) {
                // Obtener la Uri del archivo de imagen y añadirla al intent
                Uri photoURI = FileProvider.getUriForFile(
                        this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                imagePath = photoURI.getPath();
                // Iniciar la actividad de la cámara
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            // Solicitar permisos necesarios
            requestPermissionCamera();
        }
    }

    private boolean checkPermission() {
        // Comprobar si los permisos necesarios están concedidos
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void dispatchPickImageIntent() {

        if (checkPermission()) {
            // Crear el intent para la selección de imagen de la galería
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageIntent.setType("image/*"); // Solo seleccionar imágenes

            // Iniciar la actividad de la galería
            startActivityForResult(pickImageIntent, REQUEST_IMAGE_PICK);
        } else {
            // Solicitar permisos necesarios
            requestPermissionGelery();
        }
    }

    private void requestPermission() {
        requestCameraPermission();

        // Solicitar permisos necesarios
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE
        );
    }

    private void requestPermissionCamera() {
        requestCameraPermission();
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE
        );
    }

    private void requestPermissionGelery() {
        requestStoragePermission();
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE
        );
    }

    private void requestCameraPermission() {
        // Solicitar permiso para usar la cámara si no está concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        } else {
            takePicture();
        }
    }

    private void requestStoragePermission() {
        // Solicitar permiso para acceder al almacenamiento si no está concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
        } else {
            pickImage();
        }
    }

    private void pickImage() {
        // Crear un intent para seleccionar una imagen de la galería
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void takePicture() {
        // Crear un intent para tomar una foto con la cámara
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = createImageFile();

        if (photoFile != null) {
            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private File createImageFile() {
        // Crear un archivo de imagen para almacenar la foto tomada
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            File imageFile = File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", storageDir);
            // Guardar la ruta del archivo en una variable para su uso posterior
            imageUri = Uri.fromFile(imageFile);
            imagePath = imageFile.getPath();
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    imagePath = imageUri.getPath();
                    img_ejemplar.setImageURI(imageUri);
                    break;
                case REQUEST_IMAGE_PICK:


                    Uri imageUri = data.getData();
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                    try {
                        File imageFile = File.createTempFile("IMG_", ".jpg", storageDir);
                        copyImageToStorage(getContentResolver(), imageUri, imageFile);

                        Uri savedImageUri = Uri.fromFile(imageFile);
                        imagePath = savedImageUri.getPath();
                        img_ejemplar.setImageURI(savedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    private void copyImageToStorage(ContentResolver contentResolver, Uri imageUri, File imageFile) throws IOException {
        InputStream inputStream = contentResolver.openInputStream(imageUri);
        OutputStream outputStream = new FileOutputStream(imageFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        outputStream.close();
    }

    private void guardarAnimal() {

        String nombre = et_nombre_animal.getText().toString();
        String numero_animal = et_numero_animal.getText().toString();

        String fecha_nacimiento = edit_fecha_nacimiento.getText().toString();
        String raza = et_raza_animal.getText().toString();

        if (nombre.isEmpty()) {
            outlinedTextFieldNombre.setError("Se requiere el nombre del animal");
            return;
        } else {
            outlinedTextFieldNombre.isErrorEnabled();

        }

        if (numero_animal.isEmpty()) {
            outlinedTextFieldNumero.setError("Se requiere el numero del animal");
            return;
        } else {
            outlinedTextFieldNumero.isErrorEnabled();
        }

        if (raza.isEmpty()) {
            outlinedTextFieldRaza.isErrorEnabled();
            return;
        } else {
            outlinedTextFieldRaza.setEnabled(false);
        }

      // if (imagePath == null) {
      //     Toast.makeText(RegistroEspecieActivity.this, "Debe de seleccionar o tomar la imagen", Toast.LENGTH_SHORT).show();
      //     return;
      // }

        if (!isUpdate){
             if (selectedSexo.compareToIgnoreCase("NO") == 0){
                 Toast.makeText(RegistroEspecieActivity.this, "Seleccione el sexo", Toast.LENGTH_SHORT).show();
                 return;
             }

             if (selectedLote == null){
                 Toast.makeText(RegistroEspecieActivity.this, "Seleccione el lote", Toast.LENGTH_SHORT).show();
                 return;
             }
         }


        if (!isUpdate){
            final InventarioDao inventarioDao = new InventarioDao();
            final InventarioBean inventarioBean = new InventarioBean();
            inventarioBean.setNombre(nombre);
            inventarioBean.setPath_imagen(imagePath);
            inventarioBean.setCodigo_animal(numero_animal);
            inventarioBean.setFecha_nacimiento(fecha_nacimiento);
            inventarioBean.setIdLote(selectedLote.getId());
            inventarioBean.setRaza(raza);
            inventarioBean.setSexo(selectedSexo);
            inventarioDao.save(inventarioBean);
            Toast.makeText(RegistroEspecieActivity.this, "El ejemplar se registro correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            final InventarioDao inventarioDao = new InventarioDao();
            inventarioBeanEdit.setNombre(nombre);

            if (imagePath.compareToIgnoreCase("NO")  == 0){
                inventarioBeanEdit.setPath_imagen(inventarioBeanEdit.getPath_imagen());
            }else {
                inventarioBeanEdit.setPath_imagen(imagePath);
            }

            inventarioBeanEdit.setCodigo_animal(numero_animal);
            inventarioBeanEdit.setFecha_nacimiento(fecha_nacimiento);
            if (selectedLote == null){
                inventarioBeanEdit.setIdLote(inventarioBeanEdit.getId());
            }else {
                inventarioBeanEdit.setIdLote(selectedLote.getId());
            }

            inventarioBeanEdit.setRaza(raza);

            if (selectedSexo.compareToIgnoreCase("NO") == 0){
                inventarioBeanEdit.setSexo(inventarioBeanEdit.getSexo());
            }else {
                inventarioBeanEdit.setSexo(selectedSexo);
            }

            inventarioDao.save(inventarioBeanEdit);
            Toast.makeText(RegistroEspecieActivity.this, "El ejemplar se actualizo correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }


    }


}