import { User } from "./interfaces/User";

export function setCookie(key: string, value: string, daysTillExpiration: number) {
  const d = new Date();
  d.setTime(d.getTime() + daysTillExpiration * 24 * 60 * 60 * 1000);
  let expires = "expires=" + d.toUTCString();
  document.cookie = key + "=" + value + ";" + expires + ";path=/";
}

export function getCookie(key: string) {
  let name = key + "=";
  let decodedCookie = decodeURIComponent(document.cookie);
  let ca = decodedCookie.split(";");
  for (let i = 0; i < ca.length; i++) {
    let c = ca[i];
    while (c.charAt(0) == " ") {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return null;
}

export function deleteCookie(key: string, path: string = "", domain: string = "") {
  if( getCookie( key ) ) {
    document.cookie = key + "=" +
      ((path) ? ";path="+path:"")+
      ((domain)?";domain="+domain:"") +
      ";expires=Thu, 01 Jan 1970 00:00:01 GMT";
  }
}

export function getToken() {
    return getCookie("token")
}

export function deleteToken() {
  deleteCookie("token")
}

export function isAuthenticated() {
    return getToken() != null
}

export function getSelf(): User {
  return JSON.parse(getCookie("self") || '')
}
