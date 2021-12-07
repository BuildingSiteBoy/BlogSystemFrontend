import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

/**
 * 这里我们还用到了localStorage，即本地存储，在项目打开的时候会判断本地存储中是否有user这个对象存在，
 * 如果存在就取出来并获得 username 的值，否则则把 username 设置为空。
 * 这样我们只要不清除缓存，登录的状态就会一直保存。
 */
export default new Vuex.Store({
  state: {
    username: window.localStorage.getItem('username') == null ? '' : JSON.parse(window.localStorage.getItem('username' || '[]')),
    adminMenus: []
  },
  mutations: {
    initAdminMenu (state, menus) {
      state.adminMenus = menus
    },
    login (state, data) {
      state.username = data
      window.localStorage.setItem('username', JSON.stringify(data))
    },
    logout (state) {
      // 注意不能用 null 清除，否则将无法判断 user 里具体的内容
      state.username = ''
      window.localStorage.removeItem('username')
      state.adminMenus = []
    }
  },
  actions: {
  }
})

/*adminMenus: []
  },

  mutations: {
    initAdminMenu(state, menus) {
      state.adminMenus = menus
    },
    login (state, user) {
      state.user = user
      window.localStorage.setItem('user', JSON.stringify(user))
    },
    logout(state) {
      //注意不能用null清除，否则将无法判断user里具体的内容
      state.user.username = ''
      window.localStorage.removeItem('user')
      state.adminMenus = []
    }
  }
})*/
