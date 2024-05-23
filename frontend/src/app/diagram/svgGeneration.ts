export function generateObjectiveSVG(title: string, teamName: string, iconFunction: any) {
  let svg = `
<svg xmlns="http://www.w3.org/2000/svg" width="160" height="160" viewBox="0 0 160 160">
  <defs>
    <linearGradient id="half" gradientTransform="rotate(90)">
      <stop offset="80%" stop-color="#3FA8E0"/>
      <stop offset="50%" stop-color="#E5E8EB"/>
    </linearGradient>
  </defs>
  <g fill="none" fill-rule="evenodd">
    <circle cx="80" cy="80" r="80" fill="url('#half')"/>
  </g>

<svg x="70px" y="10px" width="20" height="20" viewBox="0 0 20 20" fill="none" xmlns="http://www.w3.org/2000/svg">
 ${iconFunction}
</svg>

  <foreignObject x="30" y="25" width="100px" height="100px">
    <div xmlns="http://www.w3.org/1999/xhtml"
         style="width: 100%; height: 100%; display: flex; justify-content: center; align-items: center">
      <p xmlns="http://www.w3.org/1999/xhtml">
        ${title}
      </p>
      <style>
        p {
          font-size: 16px;
          font-family: Roboto, sans-serif;
          color: black;
          display: -webkit-box;
          -webkit-line-clamp: 4;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
          word-break: break-word;
          text-align: center;
        }
      </style>
    </div>
  </foreignObject>

  <foreignObject x="30" y="90" width="100px" height="100px" style="text-anchor: middle; dominant-baseline: middle">
    <div xmlns="http://www.w3.org/1999/xhtml"
         style="width: 100%; height: 100%; display: flex; justify-content: center; align-items: center">
      <p xmlns="http://www.w3.org/1999/xhtml"
         style="font-size:12px; font-family: Roboto, sans-serif; color: black">${teamName}</p>
    </div>
  </foreignObject>
</svg>
`;

  return 'data:image/svg+xml;base64,' + btoa(svg);
}

export function generateKeyResultSVG(title: string, teamName: string, backgroundColor: any, fontColor: any) {
  let svg = `
  <svg xmlns="http://www.w3.org/2000/svg" width="120" height="120" viewBox="0 0 120 120">
  <defs>
    <linearGradient id="half" gradientTransform="rotate(90)">
      <stop offset="80%" stop-color="${backgroundColor}"/>
      <stop offset="50%" stop-color="#E5E8EB"/>
    </linearGradient>
  </defs>
  <g fill="none" fill-rule="evenodd">
    <circle cx="60" cy="60" r="60" fill="url('#half')"/>
  </g>

  <foreignObject x="18" y="12" width="80px" height="80px">
    <div xmlns="http://www.w3.org/1999/xhtml"
         style="width: 100%; height: 100%; display: flex; justify-content: center; align-items: center">
      <p xmlns="http://www.w3.org/1999/xhtml">
        ${title}
      </p>
      <style>
        p {
          font-size: 12px;
          font-family: Roboto, sans-serif;
          color: ${fontColor};
          display: -webkit-box;
          -webkit-line-clamp: 4;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
          word-break: break-word;
          text-align: center;
        }
      </style>
    </div>
  </foreignObject>

  <foreignObject x="10" y="55" width="100px" height="100px" style="text-anchor: middle; dominant-baseline: middle">
    <div xmlns="http://www.w3.org/1999/xhtml"
         style="width: 100%; height: 100%; display: flex; justify-content: center; align-items: center">
      <p xmlns="http://www.w3.org/1999/xhtml"
         style="font-size:8px; font-family: Roboto, sans-serif; color: black">${teamName}</p>
    </div>
  </foreignObject>
</svg>
  `;

  return 'data:image/svg+xml;base64,' + btoa(svg);
}

export function generateNeutralKeyResultSVG(title: string, teamName: string) {
  let svg = `
<svg xmlns="http://www.w3.org/2000/svg" width="120" height="120" viewBox="0 0 120 120">
  <defs>
    <linearGradient id="half" gradientTransform="rotate(90)">
      <stop offset="80%" stop-color="#dfe1e2"/>
      <stop offset="50%" stop-color="#404040"/>
    </linearGradient>
  </defs>
  <g fill="none" fill-rule="evenodd">
    <circle cx="60" cy="60" r="60" fill="url('#half')"/>
  </g>

  <foreignObject x="18" y="12" width="80px" height="80px">
    <div xmlns="http://www.w3.org/1999/xhtml"
         style="width: 100%; height: 100%; display: flex; justify-content: center; align-items: center">
      <p xmlns="http://www.w3.org/1999/xhtml">
        ${title}
      </p>
      <style>
        p {
          font-size: 12px;
          font-family: Roboto, sans-serif;
          color: black;
          display: -webkit-box;
          -webkit-line-clamp: 5;
          -webkit-box-orient: vertical;
          overflow: hidden;
          text-overflow: ellipsis;
          word-break: break-word;
          text-align: center;
        }
      </style>
    </div>
  </foreignObject>

  <foreignObject x="10" y="55" width="100px" height="100px" style="text-anchor: middle; dominant-baseline: middle">
    <div xmlns="http://www.w3.org/1999/xhtml"
         style="width: 100%; height: 100%; display: flex; justify-content: center; align-items: center">
      <p xmlns="http://www.w3.org/1999/xhtml"
         style="font-size:8px; font-family: Roboto, sans-serif; color: white">${teamName}</p>
    </div>
  </foreignObject>
</svg>
  `;

  return 'data:image/svg+xml;base64,' + btoa(svg);
}

export function getDraftIcon() {
  return `
    <circle cx="10" cy="10" r="10" fill="#DB00FF"/>
    <mask id="mask0_351_11899" style="mask-type:alpha" maskUnits="userSpaceOnUse" x="0" y="0" width="20" height="20">
      <rect width="20" height="20" fill="#D9D9D9"/>
    </mask>
    <g mask="url(#mask0_351_11899)">
      <path d="M6.24637 13.6849H8.01721L12.375 9.31533L10.6042 7.56533L6.24637 11.9141V13.6849ZM12.9583 8.732L13.6866 8.00375C13.8001 7.89165 13.8569 7.76203 13.8569 7.61488C13.8569 7.46772 13.8001 7.33738 13.6866 7.22385L12.6956 6.23292C12.5835 6.11939 12.4539 6.06263 12.3068 6.06263C12.1596 6.06263 12.0293 6.11939 11.9158 6.23292L11.164 6.98473L12.9583 8.732ZM10 18.4023C8.83997 18.4023 7.75035 18.1838 6.73112 17.7469C5.71189 17.31 4.82031 16.7095 4.05637 15.9456C3.29243 15.1816 2.69199 14.29 2.25504 13.2708C1.8181 12.2516 1.59962 11.162 1.59962 10.0019C1.59962 8.82802 1.8181 7.73492 2.25504 6.72265C2.69199 5.71035 3.29243 4.82224 4.05637 4.05831C4.82031 3.29437 5.71189 2.69392 6.73112 2.25698C7.75035 1.82004 8.83997 1.60156 10 1.60156C11.1739 1.60156 12.267 1.82004 13.2793 2.25698C14.2916 2.69392 15.1797 3.29437 15.9436 4.05831C16.7076 4.82224 17.308 5.71035 17.745 6.72265C18.1819 7.73492 18.4004 8.82802 18.4004 10.0019C18.4004 11.162 18.1819 12.2516 17.745 13.2708C17.308 14.29 16.7076 15.1816 15.9436 15.9456C15.1797 16.7095 14.2916 17.31 13.2793 17.7469C12.267 18.1838 11.1739 18.4023 10 18.4023ZM10 16.3606C11.7742 16.3606 13.2775 15.7444 14.51 14.5119C15.7424 13.2794 16.3587 11.7761 16.3587 10.0019C16.3587 8.22779 15.7424 6.72447 14.51 5.49198C13.2775 4.25949 11.7742 3.64325 10 3.64325C8.22585 3.64325 6.72253 4.25949 5.49004 5.49198C4.25756 6.72447 3.64131 8.22779 3.64131 10.0019C3.64131 11.7761 4.25756 13.2794 5.49004 14.5119C6.72253 15.7444 8.22585 16.3606 10 16.3606Z" fill="white"/>
    </g>
    `;
}

export function getOnGoingIcon() {
  return `
  <circle cx="10" cy="10" r="10" fill="#1E5A96"/>
  <mask id="mask0_351_11887" style="mask-type:alpha" maskUnits="userSpaceOnUse" x="0" y="0" width="20" height="20">
    <rect width="20" height="20" fill="#D9D9D9"/>
  </mask>
  <g mask="url(#mask0_351_11887)">
    <path d="M10.003 18.7297C8.80174 18.7297 7.6718 18.5037 6.61322 18.0517C5.55464 17.5997 4.6274 16.9758 3.83151 16.1799C3.03561 15.384 2.41166 14.4561 1.95968 13.3963C1.50768 12.3365 1.28168 11.2041 1.28168 9.99908C1.28168 8.7941 1.50768 7.66579 1.95968 6.61415C2.41166 5.56252 3.03505 4.63865 3.82982 3.84254C4.6246 3.04644 5.55216 2.42053 6.61249 1.96481C7.67282 1.5091 8.80579 1.28125 10.0114 1.28125C11.217 1.28125 12.3464 1.5091 13.3997 1.96481C14.453 2.42053 15.3767 3.04644 16.171 3.84254C16.9652 4.63865 17.5884 5.56499 18.0403 6.62154C18.4923 7.67811 18.7183 8.80704 18.7183 10.0083C18.7183 11.2096 18.4923 12.3396 18.0403 13.3981C17.5884 14.4567 16.9644 15.384 16.1685 16.1799C15.3726 16.9758 14.4464 17.5997 13.3898 18.0517C12.3333 18.5037 11.2043 18.7297 10.003 18.7297ZM10 16.8646C11.8998 16.8646 13.5189 16.1974 14.8574 14.8628C16.1958 13.5283 16.8651 11.9111 16.8651 10.0114C16.8651 8.11159 16.1958 6.49247 14.8574 5.154C13.5189 3.81554 11.8998 3.14631 10 3.14631C8.10025 3.14631 6.48113 3.81554 5.14266 5.154C3.80418 6.49247 3.13495 8.11159 3.13495 10.0114C3.13495 11.9111 3.80418 13.5283 5.14266 14.8628C6.48113 16.1974 8.10025 16.8646 10 16.8646ZM10.0014 15.5531C8.46241 15.5531 7.15382 15.0144 6.07561 13.9371C4.99742 12.8598 4.45832 11.5517 4.45832 10.0127C4.45832 8.47378 4.99696 7.16519 6.07424 6.08698C7.15152 5.00877 8.45965 4.46967 9.99864 4.46967C11.5376 4.46967 12.8462 5.00831 13.9244 6.08558C15.0026 7.16287 15.5417 8.47101 15.5417 10.01C15.5417 11.549 15.0031 12.8575 13.9258 13.9358C12.8485 15.014 11.5404 15.5531 10.0014 15.5531ZM9.99845 13.688C11.0188 13.688 11.8869 13.3306 12.6028 12.6157C13.3187 11.9009 13.6767 11.0333 13.6767 10.0129C13.6767 8.99256 13.3192 8.12247 12.6044 7.40267C11.8896 6.68285 11.0219 6.32294 10.0016 6.32294C8.98121 6.32294 8.11309 6.68285 7.3972 7.40267C6.68131 8.12247 6.32336 8.99256 6.32336 10.0129C6.32336 11.0333 6.68079 11.9009 7.39564 12.6157C8.11047 13.3306 8.97807 13.688 9.99845 13.688ZM9.99382 11.8646C9.47923 11.8646 9.04077 11.684 8.67845 11.3226C8.31612 10.9613 8.13495 10.5233 8.13495 10.0087C8.13495 9.49411 8.31818 9.05506 8.68464 8.69156C9.05109 8.32806 9.49162 8.14631 10.0062 8.14631C10.5208 8.14631 10.9593 8.32953 11.3216 8.69598C11.6839 9.06244 11.8651 9.50297 11.8651 10.0176C11.8651 10.5321 11.6818 10.9686 11.3154 11.327C10.9489 11.6854 10.5084 11.8646 9.99382 11.8646Z" fill="white"/>
  </g>
    `;
}

export function getSuccessfulIcon() {
  return `
  <circle cx="10" cy="10" r="10" fill="#199F03"/>
  <mask id="mask0_351_11889" style="mask-type:alpha" maskUnits="userSpaceOnUse" x="0" y="0" width="20" height="20">
    <rect width="20" height="20" fill="#D9D9D9"/>
  </mask>
  <g mask="url(#mask0_351_11889)">
    <path d="M10 14.0019C10.8647 14.0019 11.6537 13.7729 12.3669 13.3149C13.08 12.8569 13.6301 12.2402 14.0172 11.4648H5.98279C6.36986 12.2402 6.91998 12.8569 7.63315 13.3149C8.34631 13.7729 9.13526 14.0019 10 14.0019ZM6.75633 9.20575L7.5 8.46208L8.24367 9.20575L9.08152 8.37965L7.5 6.79813L5.91848 8.37965L6.75633 9.20575ZM11.7563 9.20575L12.5 8.46208L13.2437 9.20575L14.0815 8.37965L12.5 6.79813L10.9185 8.37965L11.7563 9.20575ZM10 18.4023C8.84782 18.4023 7.76207 18.1842 6.74275 17.7479C5.72342 17.3116 4.82954 16.7092 4.06112 15.9408C3.29271 15.1724 2.69035 14.2785 2.25406 13.2592C1.81777 12.2399 1.59962 11.1541 1.59962 10.0019C1.59962 8.83587 1.81777 7.74665 2.25406 6.73427C2.69035 5.72188 3.29271 4.83148 4.06112 4.06306C4.82954 3.29465 5.72342 2.69229 6.74275 2.256C7.76207 1.81971 8.84782 1.60156 10 1.60156C11.1661 1.60156 12.2553 1.81971 13.2677 2.256C14.2801 2.69229 15.1705 3.29465 15.9389 4.06306C16.7073 4.83148 17.3096 5.72188 17.7459 6.73427C18.1822 7.74665 18.4004 8.83587 18.4004 10.0019C18.4004 11.1541 18.1822 12.2399 17.7459 13.2592C17.3096 14.2785 16.7073 15.1724 15.9389 15.9408C15.1705 16.7092 14.2801 17.3116 13.2677 17.7479C12.2553 18.1842 11.1661 18.4023 10 18.4023ZM10 16.3606C11.7585 16.3606 13.2579 15.7405 14.4982 14.5001C15.7385 13.2598 16.3587 11.7604 16.3587 10.0019C16.3587 8.24348 15.7385 6.74408 14.4982 5.50375C13.2579 4.26342 11.7585 3.64325 10 3.64325C8.24154 3.64325 6.74215 4.26342 5.50181 5.50375C4.26148 6.74408 3.64131 8.24348 3.64131 10.0019C3.64131 11.7604 4.26148 13.2598 5.50181 14.5001C6.74215 15.7405 8.24154 16.3606 10 16.3606Z" fill="white"/>
  </g>
    `;
}

export function getNotSuccessfulIcon() {
  return `
  <circle cx="10" cy="10" r="10" fill="#FF0000"/>
  <mask id="mask0_351_11894" style="mask-type:alpha" maskUnits="userSpaceOnUse" x="0" y="0" width="20" height="20">
    <rect width="20" height="20" fill="#D9D9D9"/>
  </mask>
  <g mask="url(#mask0_351_11894)">
    <path d="M12.6558 9.59615C13.0344 9.59615 13.3531 9.46676 13.6119 9.208C13.8706 8.94925 14 8.63056 14 8.25194C14 7.87331 13.8706 7.55463 13.6119 7.29588C13.3531 7.03711 13.0364 6.90773 12.6617 6.90773C12.287 6.90773 11.9703 7.03815 11.7115 7.29898C11.4527 7.55981 11.3234 7.87654 11.3234 8.24917C11.3234 8.62179 11.4529 8.93945 11.712 9.20213C11.9711 9.46481 12.2857 9.59615 12.6558 9.59615ZM7.33831 9.59615C7.71302 9.59615 8.02975 9.46573 8.2885 9.2049C8.54726 8.94406 8.67665 8.62733 8.67665 8.25471C8.67665 7.88208 8.5471 7.56443 8.28802 7.30175C8.02894 7.03907 7.71433 6.90773 7.34421 6.90773C6.96558 6.90773 6.64689 7.03711 6.38812 7.29588C6.12937 7.55463 6 7.87331 6 8.25194C6 8.63056 6.12937 8.94925 6.38812 9.208C6.64689 9.46676 6.96362 9.59615 7.33831 9.59615ZM10.0021 11.0019C9.04237 11.0019 8.17014 11.2728 7.38542 11.8144C6.60069 12.3561 6.0625 13.0853 5.77083 14.0019H7.62863C7.88954 13.5865 8.23179 13.2621 8.65538 13.0287C9.07897 12.7953 9.52718 12.6786 10 12.6786C10.4728 12.6786 10.921 12.7953 11.3446 13.0287C11.7682 13.2621 12.1105 13.5865 12.3714 14.0019H14.2292C13.9375 13.0853 13.4 12.3561 12.6167 11.8144C11.8334 11.2728 10.9618 11.0019 10.0021 11.0019ZM10.0048 18.4023C8.84941 18.4023 7.76207 18.1842 6.74275 17.7479C5.72342 17.3116 4.82954 16.7092 4.06112 15.9408C3.29271 15.1724 2.69035 14.2785 2.25406 13.2591C1.81777 12.2397 1.59962 11.1505 1.59962 9.99142C1.59962 8.83236 1.81777 7.74665 2.25406 6.73427C2.69035 5.72188 3.29271 4.83148 4.06112 4.06306C4.82954 3.29465 5.72345 2.69229 6.74285 2.256C7.76226 1.81971 8.85148 1.60156 10.0105 1.60156C11.1696 1.60156 12.2553 1.81971 13.2677 2.256C14.2801 2.69229 15.1705 3.29465 15.9389 4.06306C16.7073 4.83148 17.3096 5.72376 17.7459 6.73992C18.1822 7.75607 18.4004 8.84182 18.4004 9.99717C18.4004 11.1525 18.1822 12.2399 17.7459 13.2592C17.3096 14.2785 16.7073 15.1724 15.9389 15.9408C15.1705 16.7092 14.2782 17.3116 13.262 17.7479C12.2459 18.1842 11.1601 18.4023 10.0048 18.4023ZM10 16.3606C11.7585 16.3606 13.2579 15.7405 14.4982 14.5001C15.7385 13.2598 16.3587 11.7604 16.3587 10.0019C16.3587 8.24348 15.7385 6.74408 14.4982 5.50375C13.2579 4.26342 11.7585 3.64325 10 3.64325C8.24154 3.64325 6.74215 4.26342 5.50181 5.50375C4.26148 6.74408 3.64131 8.24348 3.64131 10.0019C3.64131 11.7604 4.26148 13.2598 5.50181 14.5001C6.74215 15.7405 8.24154 16.3606 10 16.3606Z" fill="white"/>
  </g>
    `;
}
