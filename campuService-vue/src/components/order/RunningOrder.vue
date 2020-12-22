<template>
  <div>
    <!-- 面包屑导航区域 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>接手订单</el-breadcrumb-item>
      <el-breadcrumb-item>进行中订单</el-breadcrumb-item>
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
        <el-table-column label="发单方ID" prop="posterId"></el-table-column>
        <el-table-column label="价格" prop="price"></el-table-column>
        <el-table-column label="发单时间" prop="postTime"></el-table-column>
        <el-table-column label="接单时间" prop="acceptTime"></el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <span>{{ scope.row.status | statusFilter }}</span>
          </template>
        </el-table-column>
        <el-table-column label="上传">
          <template slot-scope="scope">
            <el-upload
              :action="uploadURL"
              :before-upload="beforeUpload"
              :headers="headersObj"
              :data="{orderId: scope.row.orderId}"
              :limit=1  >
              <div class="el-upload__text"><em>点击上传</em></div>
            </el-upload>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleFinish(scope.row.orderId)">完成</el-button>
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
        ordersList: [],
        uploadURL: 'api/order/uploadAccepterPhoto',
        headersObj: {
          Authorization: window.sessionStorage.getItem('token')
        },
      }
    },
    created() {
      this.getRunningOrdersList()
    },
    methods: {
      beforeUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },
      async getRunningOrdersList() {
        const { data: res } = await this.$http.get('api/order/getRunningOrder')
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
        })
      },
      async handleFinish(orderId) {
        const {data: res} = await this.$http.get('api/order/finishOrder', { params: { orderId: orderId }})
        const {code} = res
        if (code !== '200') {
          return this.$message.error('操作失败！')
        }
        this.$message.success('操作成功！')
        await location.reload()
      }
    }
  }
</script>

<style>

</style>
