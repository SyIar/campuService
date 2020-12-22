<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>接手订单</el-breadcrumb-item>
      <el-breadcrumb-item>搜索订单</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图 -->
    <el-card>
      <el-row style="margin-bottom: 20px">
        <el-col :span="3">
          <el-select v-model="start" placeholder="请选择起点">
            <el-option
              v-for="item in map"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select v-model="destination" placeholder="请选择终点">
            <el-option
              v-for="item in map"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="getSearchList" :disabled=flag>搜索</el-button>
        </el-col>
      </el-row>
      <el-table :data="ordersList" border stripe>
        <template slot="empty">
          <span>暂无数据</span>
        </template>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="订单ID" prop="orderId"></el-table-column>
        <el-table-column label="起点">
          <template slot-scope="scope">
            <el-tag>{{ scope.row.start | mapFilter }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="终点">
          <template slot-scope="scope">
            <el-tag>{{ scope.row.destination | mapFilter }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="大小">
          <template slot-scope="scope">
            <span>{{ scope.row.size | sizeFilter }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发单方ID" prop="posterId"></el-table-column>
        <el-table-column label="价格" prop="price"></el-table-column>
        <el-table-column label="发单时间" prop="postTime"></el-table-column>
        <el-table-column label="发单方文件">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleURL(scope.row.posterPhotoUrl)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleAccept(scope.row.orderId)">接单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
  export default {
    filters: {
      mapFilter(index) {
        const map = {
          0: '邯郸',
          1: '江湾',
          2: '枫林',
          3: '张江'
        }
        return map[index]
      },
      sizeFilter(index) {
        const map = {
          0: '大型',
          1: '中型',
          2: '小型'
        }
        return map[index]
      }
    },
    data() {
      return {
        map: [{
          value: '0',
          label: '邯郸'
        }, {
          value: '1',
          label: '江湾'
        }, {
          value: '2',
          label: '枫林'
        }, {
          value: '3',
          label: '张江'
        }],
        start: '',
        destination: '',
        ordersList: [],
        flag: this.start === this.destination
      }
    },
    created() {
    },
    methods: {
      async getSearchList() {
        const { data: res } = await this.$http.get('api/order/search', { params: { start: this.start, destination: this.destination } })
        const { code } = res
        const { data } = res
        if (code !== '200') {
          return this.$message.error('获取订单列表失败！')
        }
        this.ordersList = data
        this.ordersList.forEach(function(elem) {
          elem['postTime'] = elem['postTime'].replace('T', ' ')
          elem['postTime'] = elem['postTime'].substr(0, elem['postTime'].indexOf('.'))
        })
      },
      async handleAccept(orderId) {
        const {data: res} = await this.$http.get('api/order/acceptOrder', {
          params: {
            orderId: orderId,
            accepterId: window.sessionStorage.getItem('userId')
          }
        })
        const {code} = res
        if (code !== '200') {
          return this.$message.error('接单失败！')
        }
        this.$message.success('接单成功！')
        await this.$router.push('/run')
      },
      async handleURL(url) {
        let link = document.createElement('a')
        link.style.display = 'none'
        link.href = "api/order/getPhoto?filePath="+url
        link.setAttribute('download', 'photo.jpg')
        document.body.appendChild(link)
        link.click()
      }
    },
    watch: {
      start(value) {
        this.flag =  (this.start === this.destination) || (this.start === '') || (this.destination === '')
      },
      destination(value) {
        this.flag =  (this.start === this.destination) || (this.start === '') || (this.destination === '')
      }
    }
  }
</script>

<style>

</style>
