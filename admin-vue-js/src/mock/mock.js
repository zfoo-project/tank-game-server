import Mock from 'mockjs';

// import userMock from '@/module/user/mock/userMock.js';

const mocks = [
    // ...userMock
];

for (const mock of mocks) {
    Mock.mock(new RegExp(mock.url), mock.type || 'get', mock.response);
}

export default Mock;
