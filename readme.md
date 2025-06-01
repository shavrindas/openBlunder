

리엑트 시작하기

cd self
npm start

src/
├── assets/                # 이미지, 아이콘 등
│   ├── logo.png
│   └── favicon.ico
├── components/            # 재사용 가능한 UI 컴포넌트
│   ├── Button.js
│   ├── Header.js
│   └── Modal.js
├── pages/                 # 페이지 컴포넌트
│   ├── HomePage.js
│   ├── LoginPage.js
│   └── DashboardPage.js
├── services/              # API 요청 처리, 데이터 로직
│   ├── api.js
│   ├── authService.js
│   └── postService.js
├── hooks/                 # 커스텀 훅
│   ├── useAuth.js
│   └── useFetch.js
├── styles/                # 전역 스타일
│   ├── App.css
│   └── theme.css
├── App.js                 # 앱의 최상위 컴포넌트
└── index.js               # 앱의 진입점


