import Vue from 'vue';
import Router from 'vue-router';
import Login from './views/Login.vue';
import Register from './views/Register.vue';
import Home from '@/views/Home';

Vue.use(Router);

export const router = new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            component: Home
        },
        {
            path: '/login',
            component: Login
        },
        {
            path: '/register',
            component: Register
        },
        {
            path: '/home',
            name: 'home',
            // lazy-loaded
            component: () => import('./views/Home.vue')
        },
        {
            path: '/upload',
            name: 'upload',
            // lazy-loaded
            component: () => import('./views/Upload.vue')
        },
        {
            path: '/search',
            name: 'search',
            // lazy-loaded
            component: () => import('./views/Search.vue')
        },
        {
            path: '/management',
            name: 'management',
            // lazy-loaded
            component: () => import('./views/Management.vue')
        }

    ]
});

router.beforeEach((to, from, next) => {
    const publicPages = ['/login', '/register', '/'];
    const authRequired = !publicPages.includes(to.path);
    const loggedIn = localStorage.getItem('user');

    if (authRequired && !loggedIn) {
        next('/login');
    } else {
        next();
    }
});
