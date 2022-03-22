package magnalleexample.coffeshoptest.ui.map

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import magnalleexample.coffeshoptest.R

import android.view.LayoutInflater
import android.widget.TextView

class PlacemarkView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {
    private val nameTextView : TextView by lazy{
        findViewById<TextView>(R.id.placemark_name_text_view)
    }
    private var placemarkName : String
    init{
        val attributes = context
            .obtainStyledAttributes(attrs, R.styleable.PlacemarkView);
        placemarkName = attributes.getString(R.styleable.PlacemarkView_placemarkName)?:""
        this.orientation = VERTICAL
        initializeViews(context)
    }
    private fun initializeViews(context: Context) {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.placemark, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        nameTextView.text = placemarkName
    }
}