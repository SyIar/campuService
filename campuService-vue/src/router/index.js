import Vue from 'vue'
import VueRouter from 'vue-router'


// 实现路由懒加载
const Login = () => import('../components/Login.vue')
const Home = () => import('../components/Home.vue')
const Welcome = () => import('../components/Welcome.vue')

const Account = () => import('../components/user/Account.vue')
const PostedOrder = () => import('../components/user/PostedOrder.vue')
const AcceptedOrder = () => import('../components/user/AcceptedOrder.vue')

const Search = () => import('../components/order/Search.vue')
const PostNewOrder = () => import('../components/order/PostNewOrder.vue')
const Run = () => import('../components/order/RunningOrder.vue')
const Wait = () => import('../components/order/WaitingOrder.vue')

const Check = () => import('../components/admin/Check.vue')

Vue.use(VueRouter)

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  {
    path: '/home',
    component: Home,
    redirect: '/welcome',
    children: [
      { path: '/welcome', component: Welcome },
      { path: '/account', component: Account },
      { path: '/acceptedOrder', component: AcceptedOrder },
      { path: '/postedOrder', component: PostedOrder },
      { path: '/search', component: Search },
      { path: '/post', component: PostNewOrder },
      { path: '/run', component: Run },
      { path: '/wait', component: Wait },
      { path: '/check', component: Check}]
  }
]

const router = new VueRouter({
  routes,
})

// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to and from are Route Object,next() must be called to resolve the hook}
  // to将要访问的路径 from代表从哪个路径跳转而来
  // next 是一个函数，表示放行 next() 放行 next('/login') 强制跳转到登录页面
  // 如果是登录页，直接放行即可
  if (to.path === '/login') return next()
  // 获取token
  const tokenStr = window.sessionStorage.getItem('token')
  if (!tokenStr) return next('/login')
  next()
})

export default router
