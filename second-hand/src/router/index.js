import Vue from 'vue';
import Router from 'vue-router';

const originalReplace = Router.prototype.replace;
Router.prototype.replace = function replace(location) {
    return originalReplace.call(this, location).catch(err => err);
};
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
};

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/index'
        },
        {
            path: '/index',
            component: () => import('../views/index.vue'),
            meta: { title: '闲置物品交易平台' }
        },
        {
            path: '/search',
            component: () => import('../views/search.vue'),
            meta: { title: '闲置搜索 | 闲置物品交易平台' }
        },
        {
            path: '/sort',
            component: () => import('../views/sort.vue'),
            meta: { title: '闲置物品交易平台' }
        },
        {
            path: '/userinfo',
            component: () => import('../views/userinfo.vue'),
            meta: { title: '个人中心 | 闲置物品交易平台' }
        },
        {
            path: '/address',
            component: () => import('../views/address.vue'),
            meta: { title: '闲置搜索 | 闲置物品交易平台' }
        },
        {
            path: '/message',
            component: () => import('../views/message.vue'),
            meta: { title: '消息 | 闲置物品交易平台' }
        },
        {
            path: '/release',
            component: () => import('../views/release.vue'),
            meta: { title: '发布闲置 | 闲置物品交易平台' }
        },
        {
            path: '/details',
            component: () => import('../views/idle-details.vue'),
            meta: { title: '闲置详情 | 闲置物品交易平台' }
        },
        {
            path: '/order',
            component: () => import('../views/order.vue'),
            meta: { title: '订单详情 | 闲置物品交易平台' }
        },
        {
            path: '/login',
            component: () => import('../views/login.vue'),
            meta: { title: '登录 | 闲置物品交易平台' }
        },
        {
            path: '/sign-in',
            component: () => import('../views/sign-in.vue'),
            meta: { title: '注册 | 闲置物品交易平台' }
        },
        {
            path: '*',
            redirect: '/'
        }
    ]
});
