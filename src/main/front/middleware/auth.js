import cookieparser from 'cookieparser';

export default async ({store, redirect, req, route, $axios})=> {
    if (!req) {
        console.log('auth _ req ');
        return;
    }

    const session = req.headers.cookie && cookieparser.parse(req.headers.cookie).session;

    // console.dir(process);
    console.log('process.server', process.server);

    console.log('Cookie.get(\'session\')', session);

    const maybeReq = process.server ? req : null
    // const hasSession = maybeReq !== null && !!maybeReq.session
    const hasSession = maybeReq !== null && !!session;

    console.log('auth store.getters', store.getters['login/authenticated']);

    let maybeAuthenticated = await store.getters['login/authenticated'];
    console.log('auth maybeAuthenticated', hasSession, maybeAuthenticated);

    if (hasSession === true && maybeAuthenticated === false) {
        // const {data} = await $axios.get('/api/login/whois');
        // store.commit('login/setUser', data);
        // maybeAuthenticated = data.authenticated || false
        const data = {session};
        store.commit('login/setUser', data);
        maybeAuthenticated = true;
    }
    const currentPath = route.path;
    console.log('currentPath : ', currentPath);

    const isNotLogin = currentPath !== '/login/login';

    console.log(isNotLogin, maybeAuthenticated);
    console.log("---------------------------------------");
    if (isNotLogin && maybeAuthenticated === false) {
        redirect('/login/login');
    }
}