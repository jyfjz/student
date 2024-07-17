<template>
  <div class="card" style="margin-bottom: 10px">
    <el-input :prefix-icon="Search" v-model="courseId" style="width: 240px ; margin-right: 10px" placeholder="请输入课程ID查询"/>
    <el-button type="primary" style="margin-left: 10px" @click="load">查询</el-button>
    <el-button type="info" @click="reset">重置</el-button>
  </div>
  <div class="echarts-box" style="width: 100%; height: 300px; margin-bottom: 10px">
    <div id="main" style="width: 100%; height: 100%"></div>
  </div>
  <div class="card">
    <el-input :prefix-icon="Search" v-model="courseName" style="width: 240px ; margin-right: 10px" placeholder="请输入课程名称查询"/>
    <el-input :prefix-icon="Search" v-model="teacher" style="width: 240px ; margin-right: 10px" placeholder="请输入任课老师查询"/>
    <el-button type="primary" style="margin-left: 10px" @click="load_score">查询</el-button>
    <el-button type="info" @click="reset_score">重置</el-button>
  </div>
    <div>
    <el-table :data="scoreData" style="width: 100%" stripe show-overflow-tooltip height="100%">
      <el-table-column prop="studentId" label="序号"/>
      <el-table-column prop="studentName" label="学生名称" />
      <el-table-column prop="courseName" label="课程名称" />
      <el-table-column prop="teacher" label="任课老师" />
      <el-table-column prop="score" label="分数" />
    </el-table>
  </div>
</template>

<script lang="ts">
import * as echarts from 'echarts';
import {Ref, ref, onMounted, reactive, onUnmounted} from 'vue'
import request from "../../utils/request";
import { Search } from '@element-plus/icons-vue';

let option = {title: {
    text: ''
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    },
    formatter: function (params) {
      let tar;
      if (params[1] && params[1].value !== '-') {
        tar = params[1];
      } else {
        tar = params[2];
      }
      return tar && tar.name + '<br/>' + tar.seriesName + ' : ' + tar.value;
    }
  },
  dataZoom: [
    {
      id: 'dataZoomX',
      type: 'slider',
      xAxisIndex: [0],
      filterMode: 'filter'
    }
  ],
  xAxis: {
    axisLabel: {
      show: false // 设置为false以隐藏y轴的坐标标签
    },
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value',
  },
  series: [
    {
      data: [],
      type: 'bar',
      showBackground: true,
      backgroundStyle: {
        color: 'rgba(180, 180, 180, 0.2)'
      },
      barWidth: '20%',
    }
  ]
}
let chart

export default {
  name: "echartsBox",
  mychart() {
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    return myChart
  },
  data() {
    return {
      tableData: [],
      courseId:"",
      courseName:"概率论与数理统计",
      teacher: "",
      Search,
      scoreData: [],
      title: "选课情况总表",
    }
  },
  setup() {
    onMounted(() => {
      initChart();
    });

    onUnmounted(() => {
      this.mychart().dispose();
    });

    // 基础配置一下Echarts
    function initChart() {
      chart = echarts.init(document.getElementById("main"));
      // 把配置和数据放这里
      chart.setOption(option);
      window.onresize = function() {
        //自适应大小
        chart.resize();
      };
    }
    return { initChart };
  },
  created() {
    this.loadAll();
    this.load_score();
  },
  methods: {
    load(){
      // 请求分页查询数据
      fetch("http://localhost:9090/echarts/selectCourse?id="+this.courseId).then(res => res.json()).then(res => {
        console.log(res)
        this.tableData = res.data?.list || []
      }).then(this.flush);
    },
    reset(){
      this.courseId = "";
      this.tableData = []
      this.flush()
    },
    load_score(){
      // 请求分页查询数据
      fetch("http://localhost:9090/scoreRank/selectByCourseName?courseName="+this.courseName+"&teacher="+this.teacher).then(res => res.json()).then(res => {
        console.log(res)
        this.scoreData = res.data?.list || []
      })
    },
    reset_score(){
      this.courseName = ""
      this.scoreData = ""
      this.load_score()
    },
    flush(){
      option && chart.setOption(option = {title: {
          text: this.title
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          formatter: function (params) {
            let tar = params[0];
            return tar.axisValueLabel + ' : ' + tar.data;
          }
        },dataZoom: [
          {
            id: 'dataZoomX',
            type: 'slider',
            xAxisIndex: [0],
            filterMode: 'filter'

          }
        ],
        xAxis: {
          axisLabel: {
            show: false // 设置为false以隐藏y轴的坐标标签
          },
          type: 'category',
          data: this.tableData?.map(item => item.courseName) || []
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            data: this.tableData?.map(item => item.number) || [],
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
              color: 'rgba(100, 180, 100, 0.2)'
            },
            barWidth: '20%',
          }
        ]
      })
      return {}
    },
    loadAll(){
      // 请求分页查询数据
      fetch("http://localhost:9090/echarts/selectAll").then(res => res.json()).then(res => {
        console.log(res)
        this.tableData = res.data?.list || []
        console.log(this.tableData[0].number)
      }).then(this.flush);
    }
  }
};

</script>



