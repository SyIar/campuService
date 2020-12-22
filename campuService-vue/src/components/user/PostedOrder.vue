<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>个人中心</el-breadcrumb-item>
      <el-breadcrumb-item>已发订单</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图 -->
    <el-card>
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
        <el-table-column label="接单方ID" prop="posterId"></el-table-column>
        <el-table-column label="支出" prop="price"></el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <span>{{ scope.row.status | statusFilter }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发单时间" prop="postTime"></el-table-column>
        <el-table-column label="接单时间" prop="acceptTime"></el-table-column>
        <el-table-column label="完单时间" prop="finishTime"></el-table-column>
        <el-table-column label="关闭时间" prop="confirmTime"></el-table-column>

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
      },
      statusFilter(index) {
        const map = {
          0: '等待接单',
          1: '等待运送',
          2: '等待确认',
          3: '完成',
          4: '拒绝'
        }
        return map[index]
      }
    },
    data() {
      return {
        ordersList: []
      }
    },
    created() {
      this.getPostedOrdersList()
    },
    methods: {
      async getPostedOrdersList() {
        const { data: res } = await this.$http.get('api/order/getOrderByPoster', { params: { posterId: window.sessionStorage.getItem('userId') }})
        const { code } = res
        const { data } = res
        if (code !== '200') {
          return this.$message.error('获取订单列表失败！')
        }
        this.ordersList = data
        this.ordersList.forEach(function(elem) {
          elem['postTime'] = elem['postTime'].replace('T', ' ')
          elem['postTime'] = elem['postTime'].substr(0, elem['postTime'].indexOf('.'))
          if (elem['acceptTime'] !== null) {
            elem['acceptTime'] = elem['acceptTime'].replace('T', ' ')
            elem['acceptTime'] = elem['acceptTime'].substr(0, elem['acceptTime'].indexOf('.'))
          }
          if (elem['finishTime'] !== null) {
            elem['finishTime'] = elem['finishTime'].replace('T', ' ')
            elem['finishTime'] = elem['finishTime'].substr(0, elem['finishTime'].indexOf('.'))
          }
          if (elem['confirmTime'] !== null) {
            elem['confirmTime'] = elem['confirmTime'].replace('T', ' ')
            elem['confirmTime'] = elem['confirmTime'].substr(0, elem['confirmTime'].indexOf('.'))
          }
        })
      }
    }
  }
</script>

<style>

</style>
