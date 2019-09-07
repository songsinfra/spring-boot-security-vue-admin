import Cookie from 'js-cookie';
let _store;
export default ({store, redirect})=> {
    _store = store;
    try {
        if (isAuthenticated())  return;
        saveAuthInfoFromCookie();
        if (!isAuthenticated()) return redirect('/login/login');
    } catch (err) {
        console.log(err);
    }
}

function saveAuthInfoFromCookie(){
    const auth = Cookie.getJSON('auth');
    if(auth) setAuthentication(auth);
}

function isAuthenticated() {
    return _store.state.login.user;
}

function setAuthentication(auth) {
    _store.commit('login/setUser', auth);
}