package co.com.ceiba.mobile.pruebadeingreso.dataAccess;


import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SqliteDatabase extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserSQL.db";

    public SqliteDatabase(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creación de tablas
        db.execSQL("CREATE TABLE IF NOT EXISTS UserSQL ("
                + "id INTEGER, "
                + "name TEXT, "
                + "email TEXT, "
                + "phone TEXT, "
                + "PRIMARY KEY(id)"
                + ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("Aplicacion.TAG", "Actualizando base de datos de " + oldVersion + " to " + newVersion);

        // Al actualizar la base de datos lo que necesita hacer es agregar un archivo a la carpeta de assets y nombrarlo
        // from_1_to_2.sql con la versión de la que está actualizando a la última versión.
        try {
            for (int i = oldVersion; i < newVersion; ++i) {
                String migrationName = String.format("from_%d_to_%d.sql", i, (i + 1));
                Log.d("Aplicacion.TAG", "Buscando archivo de migración: " + migrationName);
                readAndExecuteSQLScript(db, migrationName);
            }
        } catch (Exception exception) {
            Log.e("Aplicacion.TAG", "Exception corriendo el script de actualización:", exception);
        }
    }

    @Override
    public synchronized void close() {
        try {
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        super.close();
    }

    /**
     * Método para ejecutar queries tipo INSERT
     * @param sql Query en formato SQL para ser ejecutado
     * @return true or false
     */
    synchronized boolean writeDb(String sql){
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                db.execSQL(sql);
                return true;
            } else {
                Log.e("Aplicacion.TAG", "Error en writeDb: No se pudo abrir conexión con la base de datos, SQL: " + sql);
                return false;
            }
        } catch (Exception e) {
            Log.e("Aplicacion.TAG", "Error en writeDb: " + e.toString() + " SQL: " + sql);
            return false;
        }
    }

    /**
     * Método para realizar consultas en BD
     * @param sql Query en formato SQL para ser ejecutado
     * @return Cursor con el resulset del query
     */
    synchronized Cursor readDb(String sql){
        Cursor cursor = null;
        try {
            db = this.getWritableDatabase();
            if (db != null) {
                cursor = db.rawQuery(sql, null);
            } else {
                Log.e("Aplicacion.TAG", "Error en readDb: No se pudo abrir conexión con la base de datos, SQL: " + sql);
            }
        } catch (Exception e) {
            Log.e("Aplicacion.TAG", "Error en readDb: " + e.toString() + " SQL: " + sql);
        }
        return cursor;
    }

    private void readAndExecuteSQLScript(SQLiteDatabase db, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            Log.d("Aplicacion.TAG", "Nombre del SQL script está vacío");
            return;
        }

        Log.d("Aplicacion.TAG", "Script encontrado. Ejecutando...");
        AssetManager assetManager = context.getAssets();
        BufferedReader reader = null;

        try {
            InputStream is = assetManager.open(fileName);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e("Aplicacion.TAG", "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("Aplicacion.TAG", "IOException:", e);
                }
            }
        }

    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                statement = new StringBuilder();
            }
        }
    }
}

