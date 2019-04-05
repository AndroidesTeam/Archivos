package com.isaacperezsegura.rwsdcard;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/*
* Clase que realiza el proceso de escritura/lectura en directorios externos publicos.
* Toda la informacion aqui presente puede obtenerse detalladamente en el siguiente enlace:
* https://developer.android.com/guide/topics/data/data-storage?hl=es-419#filesExternal
* */
public class MainActivity extends AppCompatActivity {
    // Vistas en la interfaz
    EditText name,contenido;
    TextView result;

    // Variable tipo "File" que representa el archivo a escribir
    File file;
    // Variable tipo "File" que representa la ruta en la que escribiremos
    File path;

    // FileWriter y PrintWriter utilizados para escribir cadenas en nuestro archivo creado
    FileWriter documento;
    PrintWriter writter;

    // FileReader y BufferedReader utilizados para realizar la lectura de el archivo creado
    FileReader reader;
    BufferedReader buffered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia de elementos en el layout
        name = (EditText) findViewById(R.id.name);
        contenido = (EditText) findViewById(R.id.contenido);
        result = (TextView) findViewById(R.id.result);

        /*
        *Nuestro directorio publico
        * La clase "Environment" proporciona variables de entorno en relacion con nuestra aplicacion, es
        * decir, podemos obtener informacion acerca de rutas de almacenamiento a las cuales nuestra aplicacion
        * puede acceder.
        * El metodo "getExternalStorageDirectory(TIPO DE DIRECTORIO); es un metodo que nos regresa un directorio
        * publico externo, como parametro se requiere ingresar una cadena donde especifique el tipo de directotio
        * al que deseamos acceder por ejemplo "Pictures, DCMI, Documents, Music, etc.", estas cadenas pueden
        * ser ingresadas directamente o bien obtenerlas nuevamente mediante la clase Environment, por
        * ejemplo: Environment.DIRECTORY_DOCUMENTS esta linea nos regresara la cadena "Documents".
         */
        path = new File(Environment.getExternalStoragePublicDirectory(
                //Se espera obtener una ruta como la siguiente : "/storage/sdcard/Documents/
                Environment.DIRECTORY_DOCUMENTS).getPath()
        );
    }

    /*
    *Metodo para guardar/escribir nuestros archivos:
    * Este metodo es implementado mediante el evento "OnClick" en el boton "Guardar"
     */
    public void guardarPublico(View view){
        // Primero se realiza la validacion, verificando que la memoria SD esta disponible
        if(isSDCardAvailable()){
            //validamos si el directorio donde queremos escribir existe, de no ser asi se crea
            validateDirectory();
            //Se construye nuestro archivo, especificando la ruta donde debe crearse y su nombre
            file = new File(path,name.getText().toString()+".txt");
            try {
                //Verificamos si el archivo ya existe
                if(!file.exists()) {
                    // Se realiza la escritura
                    documento = new FileWriter(file);
                    writter = new PrintWriter(documento);
                    writter.println(contenido.getText());
                    documento.close();

                    result.setText("Fichero creado en: "+path.getPath());
                    limpiarCampos();
                }else{
                    result.setText("Ese fichero ya ha sido creado asigna otro nombre");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /*
    * Metodo encargado de realizar la lectura
    */
    public void leerPublico(View view){
        // se constuye el archivo a leer, especificando la ruta y el nombre por poder ser encontrado
        file = new File(path,name.getText().toString()+".txt");

        // Se verifica si existe con el metodo "exists()" que nos regresara un true o false
        if(file.exists()) {
            try {
                //De existir se puede realizar la lectura
                reader = new FileReader(file);
                buffered = new BufferedReader(reader);
                String contenido = "";
                String linea;
                while((linea=buffered.readLine())!=null){
                    contenido += linea;
                }
                reader.close();
                //Publicamos el contenido
                this.contenido.setText(contenido);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            result.setText("Archivo no encontrado");
        }
    }
    /*
    *Metodo que valida si la memoria externa esta disponible
     */
    public boolean isSDCardAvailable(){
        /*
        * Haciendo uso de la clase Environment utilizamos el metodo "getExternalStorageState()"
        * dicho metodo devolvera el estado de la memoria SD en una cadena:
        * - Mounted: significa que la memoria esta disponible para leer y escribir en ella
        * - Removed: la memoria SD no esta disponible ni para lectura ni para escritura
        * - Mounted_ro: la memoria solo puede leerse
        * Ya que obtenemos una cadena, la verificacion debe realizarse mediante una comparacion de cadenas
        * ayudandonos con el metodo "equals" proveniente de la clase String
        *
        * Mas informacion:
        * https://developer.android.com/reference/android/os/Environment.html?hl=es-419#getExternalStorageState()
        * */
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;

    }
    /*
    * Metodo que crea el directorio donde queremos escribir nuestro archivo solo en caso de que este no exista
    * mediante el metodo "mkdir()" proveniente de la clase "File"
    * */
    public void validateDirectory(){
        if(!path.exists()){
            path.mkdir();
        }
    }
    public void limpiarCampos(){
        name.setText("");
        contenido.setText("");
    }
    public void changeActivity(View view){
        Intent intent = new Intent(this, DirectorioPrivado.class);
        startActivity(intent);
    }
}
