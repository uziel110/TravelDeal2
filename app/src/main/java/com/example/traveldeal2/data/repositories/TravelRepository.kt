
import android.app.Application
import com.example.traveldeal2.data.entities.Travel
import com.example.traveldeal2.data.repositories.TravelDataSource
import com.google.android.gms.tasks.Task


class TravelRepository : Application() {

    var remoteDataSource: TravelDataSource = TravelDataSource()

    fun insert (travel : Travel): Task<Void> {
        return remoteDataSource.insert(travel)
    }
}
