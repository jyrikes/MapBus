import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapbus.R
import com.example.mapbus.model.Rota

class RotaAdpter(private var listaRotas: ArrayList<Rota>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_PRIMEIRO = 0
    private val VIEW_TYPE_PADRAO = 1

    class RotaViewHolder(intemView: View) : RecyclerView.ViewHolder(intemView) {
        fun bind(rota: Rota) {
            itemView.findViewById<TextView>(R.id.textNomedaParada).text = rota.parada
            itemView.findViewById<TextView>(R.id.horario).text = rota.horario
        }
    }

    // ViewHolder para o primeiro item
    class PrimeiroViewHolder(intemView: View) : RecyclerView.ViewHolder(intemView) {
        fun bind(rota: Rota) {
            itemView.findViewById<TextView>(R.id.textNomedaParada).text = rota.parada
            itemView.findViewById<TextView>(R.id.horario).text = rota.horario
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_PRIMEIRO) {
            val intemView = LayoutInflater.from(parent.context).inflate(R.layout.item_primeiro, parent, false)
            PrimeiroViewHolder(intemView)
        } else {
            val intemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_rotas, parent, false)
            RotaViewHolder(intemView)
        }
    }

    override fun getItemCount(): Int {
        return listaRotas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val rota = listaRotas[position]
        if (holder is RotaViewHolder) {
            holder.bind(rota)
        } else if (holder is PrimeiroViewHolder) {
            // Se necessário, defina o conteúdo específico para o primeiro item aqui
            holder.bind(rota)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_PRIMEIRO
        } else {
            VIEW_TYPE_PADRAO
        }
    }
}
