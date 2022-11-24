package id.go.bps.sulbar.simple2.`object`

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class, Prov::class, Kab::class, Kantor_Unit::class, Pelabuhan::class, Dokumen::class, Barang::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun provDao(): ProvDao
    abstract fun kabDao(): KabDao
    abstract fun kantorUnitDao(): Kantor_UnitDao
    abstract fun pelabuhanDaou(): PelabuhanDao
    abstract fun dokumenDao(): DokumenDao
    abstract fun barangDao(): BarangDao

    companion object {
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "simple2.db")
            .build()
    }


}