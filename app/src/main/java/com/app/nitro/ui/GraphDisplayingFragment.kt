package com.app.nitro.ui

import android.content.res.Configuration
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.app.nitro.R
import com.app.nitro.databinding.FragmentGraphBinding
import com.app.nitro.domain.usecase.models.GraphData
import com.app.nitro.domain.usecase.viewmodel.GraphDataViewModel
import com.app.nitro.util.onToast
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class GraphDisplayingFragment : Fragment(), OnChartValueSelectedListener {

    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GraphDataViewModel by viewModel()
    var graphData: GraphData? = null
    private var popupWindow: PopupWindow? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO    you have to use your api cal  to get the response   Response Should lokk like
        // GraphDataModel(data=GraphData(day=
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

            showProgressbar.observe(requireActivity(), Observer { isVisible ->
            })

        }


        binding.dayGraph.setOnClickListener {

            binding.dayGraph.background = resources.getDrawable(R.drawable.bg_nitroblue_radious10dp, null)
            binding.weekGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)
            binding.monthGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)
            binding.yearGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)

            binding.dayGraph.setTextColor(resources.getColor(R.color.white))
            binding.weekGraph.setTextColor(resources.getColor(R.color.black))
            binding.monthGraph.setTextColor(resources.getColor(R.color.black))
            binding.yearGraph.setTextColor(resources.getColor(R.color.black))

            forDayGraph()

        }


        binding.weekGraph.setOnClickListener {

            binding.dayGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)
            binding.weekGraph.background = resources.getDrawable(R.drawable.bg_nitroblue_radious10dp, null)
            binding.monthGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)
            binding.yearGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)

            binding.dayGraph.setTextColor(resources.getColor(R.color.black))
            binding.weekGraph.setTextColor(resources.getColor(R.color.white))
            binding.monthGraph.setTextColor(resources.getColor(R.color.black))
            binding.yearGraph.setTextColor(resources.getColor(R.color.black))
            forWeekGraph()
        }


        binding.monthGraph.setOnClickListener {

            binding.dayGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)
            binding.weekGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)
            binding.monthGraph.background =
                resources.getDrawable(R.drawable.bg_nitroblue_radious10dp, null)
            binding.yearGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)

            binding.dayGraph.setTextColor(resources.getColor(R.color.black))
            binding.weekGraph.setTextColor(resources.getColor(R.color.black))
            binding.monthGraph.setTextColor(resources.getColor(R.color.white))
            binding.yearGraph.setTextColor(resources.getColor(R.color.black))
            forMonthGraph()
        }


        binding.yearGraph.setOnClickListener {

            binding.dayGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)
            binding.weekGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)
            binding.monthGraph.background =
                resources.getDrawable(R.drawable.bg_layout_corner_rounded_consult, null)
            binding.yearGraph.background = resources.getDrawable(R.drawable.bg_nitroblue_radious10dp, null)

            binding.dayGraph.setTextColor(resources.getColor(R.color.black))
            binding.weekGraph.setTextColor(resources.getColor(R.color.black))
            binding.monthGraph.setTextColor(resources.getColor(R.color.black))
            binding.yearGraph.setTextColor(resources.getColor(R.color.white))
            forYearGraph()
        }


    }


    fun forDayGraph() {
        val bargroup = java.util.ArrayList<BarEntry>()

        for (i in 0 until graphData?.day?.size!!) {
            bargroup.add(BarEntry(graphData?.day!![i].hour, graphData?.day!![i].value.toFloat()))
        }

        // creating dataset for Bar Group
        val barDataSet = BarDataSet(bargroup, "Day Wise")
        barDataSet.color = ContextCompat.getColor(requireActivity(), R.color.nitro_blue)

        val data = BarData(barDataSet)
        binding?.barChart.setData(data)
        binding?.barChart.xAxis.labelCount = bargroup.size  // 11
        binding?.barChart.xAxis.setGranularity(4f) // only intervals of 1 day   4 for every 4 hours to display

        relatedCodes()

        binding?.barChart.xAxis.setValueFormatter(
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

        // chart.setDrawYLabels(false);
        val xAxisFormatter: IAxisValueFormatter =
            DayAxisValueFormatter(binding?.barChart)
        val mv = XYMarkerView(requireActivity(), xAxisFormatter)
        mv.setChartView(binding?.barChart) // For bounds control
        binding?.barChart.setMarker(mv) // Set the marker to the chart


    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun forWeekGraph() {

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val bargroup = java.util.ArrayList<BarEntry>()

        for (i in 0 until graphData?.week?.size!!) {
            val date: LocalDate = LocalDate.parse(graphData?.week!![i].day, formatter)
            bargroup.add(
                BarEntry(
                    date.getDayOfWeek().value.toFloat(),
                    graphData?.week!![i].value.toFloat()
                )
            )
        }

        // creating dataset for Bar Group
        val barDataSet = BarDataSet(bargroup, "Week Wise")
        barDataSet.color = ContextCompat.getColor(requireActivity(), R.color.nitro_blue)


        val data = BarData(barDataSet)
        binding?.barChart.setData(data)
        binding?.barChart.xAxis.labelCount = bargroup.size  // 11
        binding?.barChart.xAxis.setGranularity(1f) // only intervals of 1 day   4 for every 4 hours to display

        relatedCodes()

        binding?.barChart.xAxis.setValueFormatter(
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
        val bargroup = java.util.ArrayList<BarEntry>()

        for (i in 0 until graphData?.month?.size!!) {
            val date: LocalDate = LocalDate.parse(graphData?.month!![i].day, formatter)
            bargroup.add(
                BarEntry(
                    date.getDayOfWeek().value.toFloat(),
                    graphData?.month!![i].value.toFloat()
                )
            )
        }

        // creating dataset for Bar Group
        val barDataSet = BarDataSet(bargroup, "Month Wise")
        barDataSet.color = ContextCompat.getColor(requireActivity(), R.color.nitro_blue)

        val data = BarData(barDataSet)
        binding?.barChart.setData(data)
        binding?.barChart.xAxis.labelCount = bargroup.size  // 11
        binding?.barChart.xAxis.setGranularity(3f) // only intervals of 1 day   4 for every 4 hours to display

        relatedCodes()

        binding?.barChart.xAxis.setValueFormatter(
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

        val bargroup = java.util.ArrayList<BarEntry>()

        for (i in 0 until graphData?.year?.size!!) {
            bargroup.add(
                BarEntry(
                    graphData?.year!![i].month.toFloat(),
                    graphData?.year!![i].value.toFloat()
                )
            )
        }

        // creating dataset for Bar Group
        val barDataSet = BarDataSet(bargroup, "Year Wise")
        barDataSet.color = ContextCompat.getColor(requireActivity(), R.color.nitro_blue)

        val data = BarData(barDataSet)
        binding?.barChart.setData(data)
        binding?.barChart.xAxis.labelCount = bargroup.size  // 11
        binding?.barChart.xAxis.setGranularity(1f) // only intervals of 1 day   4 for every 4 hours to display

        relatedCodes()

        binding?.barChart.xAxis.setValueFormatter(
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


    fun relatedCodes() {

        binding?.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding?.barChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
        binding?.barChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
        binding?.barChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
        binding?.barChart.description.isEnabled = false
        binding?.barChart.animateY(1000)
        binding?.barChart.legend.isEnabled = false
        binding?.barChart.setPinchZoom(true)
        binding?.barChart.data.setDrawValues(false)

        binding?.barChart.invalidate()


    }


    private val onValueSelectedRectF = RectF()
    override fun onValueSelected(e: Entry?, h: Highlight?) {
        if (e == null) return
        val bounds: RectF = onValueSelectedRectF
        binding.barChart.getBarBounds(e as BarEntry?, bounds)
        val position: MPPointF = binding.barChart.getPosition(e, YAxis.AxisDependency.LEFT)
        Log.i("bounds", bounds.toString())
        Log.i("position", position.toString())
        Log.i(
            "x-index",
            "low: " + binding.barChart.getLowestVisibleX() + ", high: " + binding.barChart.getHighestVisibleX()
        )
        MPPointF.recycleInstance(position)
    }

    override fun onNothingSelected() {}

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (popupWindow != null && popupWindow!!.isShowing) {
                popupWindow!!.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




//    private fun setLineChartData(pricesHigh: ArrayList<Entry>) {
//        val dataSets = ArrayList<ILineDataSet>()
//        val highLineDataSet = LineDataSet(pricesHigh, "Price (High)")
//        highLineDataSet.setDrawCircles(true)
//        highLineDataSet.circleRadius = 4f
//        highLineDataSet.setDrawValues(false)
//        highLineDataSet.lineWidth = 3f
//        highLineDataSet.color = Color.GREEN
//        highLineDataSet.setCircleColor(Color.GREEN)
//        dataSets.add(highLineDataSet)
//        val lineData = LineData(dataSets)
//        binding.barChart.setData(lineData)
//        binding.barChart.invalidate()
//    }


}































//    private fun initChart() {
//        binding.barChart.setDrawGridBackground(false)
//        binding.barChart.legend.isEnabled = false
//        binding.barChart.description.isEnabled = false
//        binding.barChart.axisLeft.isEnabled = true
//        binding.barChart.axisRight.isEnabled = false
//        binding.barChart.xAxis.setDrawAxisLine(false)
//        binding.barChart.xAxis.setDrawGridLines(false)
//        binding.barChart.xAxis.setDrawGridLinesBehindData(false)
//        binding.barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        binding.barChart.setDrawBorders(false)
////        binding.barChart.setFitBars(false)
//        binding.barChart.setPinchZoom(false)
//        binding.barChart.setNoDataText("")
//        binding.barChart.clear()
//    }

//    override fun updateChart(items: List<HomeChartItem>) {
//        if(activity == null) return
//        val entries = items.mapIndexed { index, homeChartItem ->
//            BarEntry(index.toFloat(), homeChartItem.amount.toFloat(), homeChartItem)
//        }
//        val barDataSet = BarDataSet(entries, "Last 3 months")
//        context?.let {
//            barDataSet.colors = listOf(
//                ContextCompat.getColor(it, R.color.chart_bar_type1),
//                ContextCompat.getColor(it, R.color.chart_bar_type2),
//                ContextCompat.getColor(it, R.color.chart_bar_type3)
//            )
//        }
//        val barData = LineData(barDataSet)
//        barData.barWidth = 0.7f
//        barData.setValueTextSize(14f)
//        barData.setDrawValues(false) // Hides the labels on bars
//        binding.barChart.xAxis.valueFormatter = object: ValueFormatter() {
//            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
//                if(value % 1 == 0.0f) {
//                    return items[value.toInt()].month.subSequence(0, 3).toString()
//                }
//                return ""
//            }
//        }
//        binding.barChart.data = barData
//        binding.barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
//            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                val month = (e?.data as HomeChartItem).month
//                val year = (e?.data as HomeChartItem).year
//                val startDate = getStartDate(month, year)
//                val endDate = getEndDate(month, year)
//                navigateToBillsList(DateRange(startDate, endDate))
//            }
//
//            override fun onNothingSelected() {
//            }
//        })
//        binding.barChart.animateY(1500)
//        binding.barChart.invalidate()
//    }


//    private fun bindAdapterCustomerList(
//        assignedcustomerlistItem: List<SensorDevice>
//    ) {
//        if (assignedcustomerlistItem != null) {
//            binding!!.rvSensors.visibility = View.VISIBLE
//            sensorsListAdapter = SensorsListAdapter(
//                assignedcustomerlistItem!!,
//                object : SensorsListAdapter.ItemSelectionListener {
//                    override fun onItemClick(
//                        position: Int,
//                        customerList: SensorDevice
//                    ) {
//
//                    }
//                }, requireActivity()
//            )
//            binding!!.rvSensors.adapter = sensorsListAdapter
//        } else {
//            binding!!.rvSensors.visibility = View.GONE
//        }
//    }

