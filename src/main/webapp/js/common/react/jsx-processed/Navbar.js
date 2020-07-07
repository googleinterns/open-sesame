/**
 * Returns a React navbar component.
 * @param {{}} props The properties of the navbar.
 * @return {React.Component} Returns the navbar.
 */function Navbar(props){var urls=[{href:'/',name:'Home'},{href:'/projects.html',name:'Projects'}];return React.createElement('nav',{className:'navbar navbar-expand-lg navbar-light bg-light'},React.createElement('a',{className:'navbar-brand',href:'#'},'Open Sesame'),React.createElement('button',{className:'navbar-toggler','data-toggle':'collapse','data-target':'#navbarSupportedContent','aria-controls':'navbarSupportedContent','aria-expanded':'false','aria-label':'Toggle navigation'},React.createElement('span',{className:'navbar-toggler-icon'})),React.createElement('div',{className:'collapse navbar-collapse',id:'navbarSupportedContent'},React.createElement('ul',{className:'navbar-nav ml-auto'},urls.map(function(url,i){return React.createElement(NavbarLink,{key:i,href:url.href,name:url.name})}))))}/**
 * Returns a React navbar link component.
 * @param {{href: string, name: string}} props
 * @return {React.Component} Returns the navbar link.
 */function NavbarLink(props){var classes='nav-item';if(props.href===window.location.pathname){classes+=' active'}return React.createElement('div',{className:classes},React.createElement('a',{className:'nav-link',href:props.href},props.name))}var navbarContainer=document.getElementById('navbar-container');ReactDOM.render(React.createElement(Navbar,null),navbarContainer);