let _store;
export default ({store, redirect})=> {
    _store = store;
    try {
        if (!isAuthenticated()) return redirect('/login/login');
    } catch (err) {
        console.log(err);
    }
}

function isAuthenticated() {
    return _store.state.login.user;
}

