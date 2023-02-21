package com.app.nitro.ui

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.app.nitro.R
import com.app.nitro.databinding.FragmentLinegraphBinding
import com.app.nitro.domain.usecase.models.GraphData
import com.app.nitro.domain.usecase.viewmodel.GraphDataViewModel
import com.app.nitro.util.onToast
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class LineGraphDisplayingFragment : Fragment() {

    private var _binding: FragmentLinegraphBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GraphDataViewModel by viewModel()
    var graphData: GraphData? = null
    var xAxis: XAxis? = null
    var yAxis: YAxis? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLinegraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



       //TODO    you have to use your api cal  to get the response
        //TODO Response Should lokk like 15:52:35.331  I
        // result: GraphDataModel(data=GraphData(day=
        // [Day(hour=0.0, value=3.0666666666666664), Day(hour=1.0, value=3.0675), Day(hour=2.0, value=3.068), Day(hour=3.0, value=3.0683333333333334), Day(hour=4.0, value=3.068), Day(hour=5.0, value=3.067), Day(hour=6.0, value=3.067), Day(hour=7.0, value=3.068), Day(hour=8.0, value=3.0686666666666667), Day(hour=9.0, value=3.0660000000000003), Day(hour=10.0, value=3.067)],
        // month=[Month(day=2023-02-01, value=3.0009565217391305), Month(day=2023-02-02, value=3.0673432835820895), Month(day=2023-02-03, value=3.067711538461538), Month(day=2023-02-04, value=3.0679166666666666), Month(day=2023-02-06, value=3.069529411764706), Month(day=2023-02-07, value=3.069551724137931), Month(day=2023-02-08, value=3.0695172413793106), Month(day=2023-02-09, value=3.069641791044776), Month(day=2023-02-10, value=3.068622222222222), Month(day=2023-02-14, value=3.0681875), Month(day=2023-02-15, value=3.068169230769231), Month(day=2023-02-16, value=3.068234375), Month(day=2023-02-17, value=3.067863636363636), Month(day=2023-02-18, value=3.0679830508474577), Month(day=2023-02-19, value=3.068076923076923), Month(day=2023-02-20, value=3.068108695652174), Month(day=2023-02-21, value=3.0675)], week=[Week(day=2023-02-19, value=3.068076923076923),
        // Week(day=2023-02-20, value=3.068108695652174), Week(day=2023-02-21, value=3.0675)], year=[Year(month=1, value=3.1369675190048376), Year(month=2, value=3.064716666666667)]), status=true, message=null)



        viewModel.getSensorAnalyticsGraph(
            "",
            "",
            "Bearer " +""
        )

        with(viewModel) {
            graphDataModel.observe(requireActivity(), Observer {
                if (it.status) {
                    if (it.data != null) {
                        graphData = it.data
                        forDayGraph()
                    }
                } else {
                    it.message.let { it1 ->
                        onToast(it1!!, requireActivity())
                    }
                }
            })

            messageData.observe(requireActivity(), Observer { it ->
                onToast(it!!, requireActivity())
                if (it.equals("Unauthorized!")) {
                }
            })

            showProgressbar.observe(requireActivity(), Observer { isVisible ->
            })
        }  // 24E124710C140625  A84041D1E185E676


        binding?.lineChart.setBackgroundColor(Color.WHITE) // background color
        binding?.lineChart.getDescription()?.isEnabled = false // disable description text
        binding?.lineChart.setTouchEnabled(true) // enable touch gestures

//        chart.setOnChartValueSelectedListener(this)        // set listeners
        binding?.lineChart.setDrawGridBackground(false)
        val mv = MyMarkerView(
            requireActivity(),
            R.layout.custom_marker_view
        ) // create marker to display box when values are selected
        mv.chartView = binding?.lineChart
        binding?.lineChart.setMarker(mv)
        binding?.lineChart.setDragEnabled(true) // enable scaling and dragging
        binding?.lineChart.setScaleEnabled(true)

//        mChart.getAxisLeft().setDrawGridLines(false)
        binding?.lineChart.getXAxis()
            .setDrawGridLines(false) //To remove x axis grid background as per design

        binding?.lineChart.setScaleXEnabled(true)
        binding?.lineChart.setScaleYEnabled(true)

        binding?.lineChart.setPinchZoom(true) // force pinch zoom along both axis

        // X-Axis Style //
        xAxis = binding?.lineChart.getXAxis()
        xAxis?.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis?.enableGridDashedLine(20f, 15f, 0f) // vertical grid lines
        xAxis?.setLabelRotationAngle(-30f)

        // // Y-Axis Style // //
        yAxis = binding?.lineChart.getAxisLeft()
        binding?.lineChart.getAxisRight()?.isEnabled =
            true // disable dual axis (only use LEFT axis)

        yAxis?.enableGridDashedLine(10f, 10f, 0f) // horizontal grid lines

//        yAxis?.setAxisMaximum(200f) // axis range
//        yAxis?.setAxisMinimum(-50f)

        yAxis?.setDrawLimitLinesBehindData(true) // draw limit lines behind data instead of on top
        xAxis?.setDrawLimitLinesBehindData(true)

        binding?.lineChart.animateX(1500) // draw points over time
        val legend =
            binding?.lineChart.getLegend() // get the legend (only possible after setting data)
        legend?.form = LegendForm.LINE // draw legend entries as lines


        binding.dayGraph.setOnClickListener {

            binding.dayGraph.background =
                resources.getDrawable(R.drawable.bg_nitroblue_radious4dp, null)
            binding.weekGraph.background = resources.getDrawable(R.drawable.bg_null, null)
            binding.monthGraph.background = resources.getDrawable(R.drawable.bg_null, null)
            binding.yearGraph.background = resources.getDrawable(R.drawable.bg_null, null)

            binding.dayGraph.setTextColor(resources.getColor(R.color.white))
            binding.weekGraph.setTextColor(resources.getColor(R.color.black))
            binding.monthGraph.setTextColor(resources.getColor(R.color.black))
            binding.yearGraph.setTextColor(resources.getColor(R.color.black))

            forDayGraph()

        }


        binding.weekGraph.setOnClickListener {

            binding.dayGraph.background = resources.getDrawable(R.drawable.bg_null, null)
            binding.weekGraph.background =
                resources.getDrawable(R.drawable.bg_nitroblue_radious4dp, null)
            binding.monthGraph.background = resources.getDrawable(R.drawable.bg_null, null)
            binding.yearGraph.background = resources.getDrawable(R.drawable.bg_null, null)

            binding.dayGraph.setTextColor(resources.getColor(R.color.black))
            binding.weekGraph.setTextColor(resources.getColor(R.color.white))
            binding.monthGraph.setTextColor(resources.getColor(R.color.black))
            binding.yearGraph.setTextColor(resources.getColor(R.color.black))
            forWeekGraph()
        }


        binding.monthGraph.setOnClickListener {

            binding.dayGraph.background = resources.getDrawable(R.drawable.bg_null, null)
            binding.weekGraph.background = resources.getDrawable(R.drawable.bg_null, null)
            binding.monthGraph.background =
                resources.getDrawable(R.drawable.bg_nitroblue_radious4dp, null)
            binding.yearGraph.background = resources.getDrawable(R.drawable.bg_null, null)

            binding.dayGraph.setTextColor(resources.getColor(R.color.black))
            binding.weekGraph.setTextColor(resources.getColor(R.color.black))
            binding.monthGraph.setTextColor(resources.getColor(R.color.white))
            binding.yearGraph.setTextColor(resources.getColor(R.color.black))
            forMonthGraph()
        }


        binding.yearGraph.setOnClickListener {

            binding.dayGraph.background = resources.getDrawable(R.drawable.bg_null, null)
            binding.weekGraph.background = resources.getDrawable(R.drawable.bg_null, null)
            binding.monthGraph.background = resources.getDrawable(R.drawable.bg_null, null)
            binding.yearGraph.background =
                resources.getDrawable(R.drawable.bg_nitroblue_radious4dp, null)

            binding.dayGraph.setTextColor(resources.getColor(R.color.black))
            binding.weekGraph.setTextColor(resources.getColor(R.color.black))
            binding.monthGraph.setTextColor(resources.getColor(R.color.black))
            binding.yearGraph.setTextColor(resources.getColor(R.color.white))
            forYearGraph()
        }


    }


    fun forDayGraph() {

        val incomeEntries = ArrayList<Entry>()
        for (i in 0 until graphData?.day?.size!!) {
            incomeEntries.add(
                Entry(
                    graphData?.day!![i].hour,
                    graphData?.day!![i].value.toFloat()
                )
            ) // Y axis values
        }
        binding?.lineChart.invalidate()
        setData(incomeEntries)

        binding?.lineChart.xAxis?.setValueFormatter(
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
                    var label = ""
                    if (value == 0f)
                        label = "12am"
                    else if (value == 1f)
                        label = "1am"
                    else if (value == 2f)
                        label = "2am"
                    else if (value == 3f)
                        label = "3am"
                    else if (value == 4f)
                        label = "4am"
                    else if (value == 5f)
                        label = "5am"
                    else if (value == 6f)
                        label = "6am"
                    else if (value == 7f)
                        label = "7am"
                    else if (value == 8f)
                        label = "8am"
                    else if (value == 9f)
                        label = "9am"
                    else if (value == 10f)
                        label = "10am"
                    else if (value == 11f)
                        label = "11am"
                    else if (value == 12f)
                        label = "12am"
                    else if (value == 13f)
                        label = "1pm"
                    else if (value == 14f)
                        label = "2pm"
                    else if (value == 15f)
                        label = "3pm"
                    else if (value == 16f)
                        label = "4pm"
                    else if (value == 17f)
                        label = "5pm"
                    else if (value == 18f)
                        label = "6pm"
                    else if (value == 19f)
                        label = "7pm"
                    if (value == 20f)
                        label = "8pm"
                    else if (value == 21f)
                        label = "9pm"
                    else if (value == 22f)
                        label = "10pm"
                    else if (value == 23f)
                        label = "11pm"
                    return label
                }
            }
        )

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun forWeekGraph() {

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val incomeEntries = ArrayList<Entry>()
        for (i in 0 until graphData?.week?.size!!) {
            val date: LocalDate = LocalDate.parse(graphData?.week!![i].day, formatter)
            incomeEntries.add(
                Entry(
                    date.getDayOfWeek().value.toFloat(),
                    graphData?.week!![i].value.toFloat()
                )
            ) // Y axis values
        }

        binding?.lineChart.invalidate()
        setData(incomeEntries)

        binding?.lineChart.xAxis?.setValueFormatter(
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
                    var label = ""
                    if (value == 1f)
                        label = "Mon"
                    else if (value == 2f)
                        label = "Tue"
                    else if (value == 3f)
                        label = "Wed"
                    else if (value == 4f)
                        label = "Thru"
                    else if (value == 5f)
                        label = "Fri"
                    else if (value == 6f)
                        label = "Sat"
                    else if (value == 7f)
                        label = "Sun"

                    return label
                }
            }
        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun forMonthGraph() {

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val incomeEntries = ArrayList<Entry>()
        for (i in 0 until graphData?.month?.size!!) {
            val date: LocalDate = LocalDate.parse(graphData?.month!![i].day, formatter)
            incomeEntries.add(
                Entry(
                    date.getDayOfWeek().value.toFloat(),
                    graphData?.month!![i].value.toFloat()
                )
            ) // Y axis values
        }

        binding?.lineChart.invalidate()
        setData(incomeEntries)

        binding?.lineChart.xAxis?.setValueFormatter(
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
                    var label = ""
                    if (value == 1f)
                        label = "Mon"
                    else if (value == 2f)
                        label = "Tue"
                    else if (value == 3f)
                        label = "Wed"
                    else if (value == 4f)
                        label = "Thru"
                    else if (value == 5f)
                        label = "Fri"
                    else if (value == 6f)
                        label = "Sat"
                    else if (value == 7f)
                        label = "Sun"

                    return label
                }
            }
        )


    }

    fun forYearGraph() {
        val incomeEntries = ArrayList<Entry>()
        for (i in 0 until graphData?.year?.size!!) {
            incomeEntries.add(
                Entry(
                    graphData?.year!![i].month.toFloat(),
                    graphData?.year!![i].value.toFloat()
                )
            ) // Y axis values
        }

        binding?.lineChart.invalidate()
        setData(incomeEntries)

        binding?.lineChart.xAxis?.setValueFormatter(
            object : ValueFormatter() {
                override fun getAxisLabel(value: Float, axis: AxisBase?): String? {
                    var label = ""

                    if (Math.round(value) == 1) {
                        label = "JAN"
                    } else if (Math.round(value) == 2) {
                        label = "FEB"
                    } else if (Math.round(value) == 3) {
                        label = "MAR"
                    } else if (Math.round(value) == 4) {
                        label = "APR"
                    } else if (Math.round(value) == 5) {
                        label = "MAY"
                    } else if (Math.round(value) == 6) {
                        label = "JUN"
                    } else if (Math.round(value) == 7) {
                        label = "JUL"
                    } else if (Math.round(value) == 8) {
                        label = "AUG"
                    } else if (Math.round(value) == 9) {
                        label = "SEP"
                    } else if (Math.round(value) == 10) {
                        label = "OCT"
                    } else if (Math.round(value) == 11) {
                        label = "NOV"
                    } else if (Math.round(value) == 12) {
                        label = "DEC"
                    }

                    return label
                }
            }
        )

    }

    private fun setData(incomeEntries: ArrayList<Entry>) {

        val set1: LineDataSet
        if (binding?.lineChart.data != null && binding?.lineChart.data.dataSetCount > 0) {
            set1 = binding?.lineChart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = incomeEntries
            set1.notifyDataSetChanged()
            binding?.lineChart.data.notifyDataChanged()
            binding?.lineChart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(incomeEntries, "")
            set1.setDrawIcons(false)
            // draw dashed line
            set1.enableDashedLine(0f/*10f*/, 0f/*5f*/, 0f)
            set1.color =
                resources.getColor(R.color.graph_green_color)// Color.BLACK // black lines and points
            set1.setCircleColor(Color.BLACK)
            set1.lineWidth = 2f // line thickness and point size
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false) // draw points as solid circles
            set1.formLineWidth = 1f // customize legend entry
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            set1.valueTextSize = 9f // text size of values
            set1.enableDashedHighlightLine(10f, 5f, 0f) // draw selection line as dashed

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER)        //to make the smooth line as the graph is adrapt change so smooth curve
            set1.setCubicIntensity(0.2f)        //to enable the cubic density : if 1 then it will be sharp curve


            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> binding?.lineChart.axisLeft.axisMinimum }
            if (Utils.getSDKInt() >= 18) {            // set color of filled area
                // drawables only supported on api level 18 and above
                val drawable =
                    ContextCompat.getDrawable(requireActivity(), R.drawable.green_gradient)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets
            val data = LineData(dataSets) // create a data object with the data sets
            binding?.lineChart.data = data // set data
        }

    }


    //        redraw
    //        chart.invalidate()


}

