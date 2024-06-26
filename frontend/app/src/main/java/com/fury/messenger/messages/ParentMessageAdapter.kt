
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fury.messenger.R
import com.fury.messenger.crypto.Crypto
import com.fury.messenger.data.db.model.Chat
import com.fury.messenger.messages.MessageAdapter
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.crypto.SecretKey

class  ChatsByDate(val date:LocalDate,var data: ArrayList<Chat> = arrayListOf())
class ParentAdapter(var messageList: ArrayList<ChatsByDate?>, val context: Context, var uid: String?, var recipientKey: SecretKey?,private  val setSelected: (String?) -> Unit) : RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_layout, parent, false)
        return ParentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val childData = messageList[position]
        if (childData != null) {
            holder.setupChildRecyclerView(childData)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    inner class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val childRecyclerView: RecyclerView = itemView.findViewById(R.id.chatRecyclerView)
        private val date: TextView =itemView.findViewById(R.id.chatdatetime)


        @SuppressLint("SetTextI18n")
        fun setupChildRecyclerView(data: ChatsByDate) {

            if(recipientKey!=null){
                Log.d("Key ParentMessageAdapter", Crypto.convertAESKeyToString(recipientKey!!))

            }
            childRecyclerView.isNestedScrollingEnabled = false

            childRecyclerView.smoothScrollToPosition(data.data.size-1)
            val layoutManager = LinearLayoutManager(itemView.context)
            childRecyclerView.layoutManager = layoutManager


            val adapter = MessageAdapter(context,data,uid,recipientKey,setSelected)
            childRecyclerView.adapter = adapter

            date.text= data.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

            if(Period.between(LocalDate.now(),data.date).days==-1){
                date.text="Yesterday"

            }
            else  if(data.date.isEqual(LocalDate.now())){
                date.text="Today"

            }
        }
    }
}
