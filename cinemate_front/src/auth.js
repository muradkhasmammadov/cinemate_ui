import jwt_decode from 'jwt-decode';

export default {
  user: {
    authenticated: false,
    role: '',
    username: '',
  },
  authenticated: async function () {
    const token = localStorage.getItem('jwtToken');
    // if there is a token, get the user role, and try to authinticate
    if (token) {
      this.user.role = jwt_decode(token).role;
      console.log('role ' + this.user.role);
      // Set the Authorization header with the token value
      const headers = {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      };
      await fetch('http://localhost:8080/auth/authenticate', { headers })
        .then((response) => response.json())
        .then((data) => {
          this.user.authenticated = data;
          console.log(data);
        })
        .catch((err) => console.log(err.message));
      return this.user.authenticated; //returns a boolean value
    }
    // there is no token, we cannot authinticate
    else return false;
  },

  hasAnyOf: function (role) {
    if (this.user.role) return this.user.role == role;
  },

  logout: function () {
    localStorage.removeItem('jwtToken');
    this.user = { role: '', username: '', authenticated: false };
  },
};
