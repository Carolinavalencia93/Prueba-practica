package co.com.ceiba.mobile.pruebadeingreso.dataAccess;

import android.content.Context;
import android.database.Cursor;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.models.Users;

public class SqliteRoutines extends SqliteDatabase {

    List<Users> usersList = null;

    public SqliteRoutines(Context context) {
        super(context);
    }


    public boolean saveUsers(Users users) {
        String query = null;
        if (users != null) {
            query = "INSERT INTO UserSQL (id, name, email,phone) " + "VALUES ('" +
                    users.getId() + "', '" +
                    users.getname() + "', '" +
                    users.getEmail() + "', '" +
                    users.getphone() + "')";

        }
        return writeDb(query);
    }

    public JSONArray readUsers() {
        JSONArray  usuarios = new JSONArray();
        // Crea y ejecuta el query
        String query = "SELECT * FROM UserSQL ;";
        Cursor cursor = readDb(query);

        // Verifica el cursor
        if (cursor != null) {
            int numeroOrdenes = cursor.getCount();
            try {
                if (cursor.moveToFirst()) {
                    usersList = new ArrayList<Users>(numeroOrdenes);
                    do {


                        int id = cursor.getInt(0);
                        String name = cursor.getString(1);
                        String email = cursor.getString(2);
                        String phone = cursor.getString(3);


                        JSONObject item1 = new JSONObject();
                        item1.put("id", id);
                        item1.put("name", name);
                        item1.put("email",email);
                        item1.put("phone", phone);
                        usuarios.put(item1);

                    } while(cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!cursor.isClosed()) {
                        cursor.close();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        // Retorno
        return usuarios;
    }

}