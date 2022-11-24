package id.go.bps.sulbar.simple2.`object`

import androidx.lifecycle.LiveData
import androidx.room.*
import id.go.bps.sulbar.simple2.repository.URL
import java.lang.System.currentTimeMillis

@Entity
data class User (
    @PrimaryKey var id: Int,
    var nama: String,
    var username: String,
    var prov_id: Int?,
    var kab_id: Int?,
    var kantor_unit_id: Int?,
    var pelabuhan_id: Int?,
    var leveluser_id: Int,
    var token: String,
    var date_login: Long,
    var date_refresh: Long,
    var status: Int // 0 normal, 1 need login
){
    fun isRefreshExpired():Boolean{
        return ((System.currentTimeMillis()-date_refresh)/60000) > (URL.JWT_TTL-1)
    }
    fun isRefreshTTLExpired():Boolean{
        return ((System.currentTimeMillis()-date_login)/60000) > (URL.JWT_REFRESH_TTL-1)
    }
}

@Dao
interface UserDao {
    @Insert
    fun insertAll(vararg items: User)

    @Update
    fun update(vararg users: User)

    @Delete
    fun delete(items: User)

    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("DELETE FROM User")
    fun nukeTable();

    @Query ("SELECT * FROM User LIMIT 1")
    fun getActiveUserNotLive () : User

    @Query ("SELECT * FROM User LIMIT 1")
    fun getActiveUser () : LiveData<User>
}