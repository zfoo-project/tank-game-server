const nodeEnv = process.env.NODE_ENV === 'development';

export function isDevelopment() {
    return nodeEnv;
}
