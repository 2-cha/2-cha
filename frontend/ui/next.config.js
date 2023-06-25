const path = require('path');

/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  trailingSlash: true,
  sassOptions: {
    includePaths: [path.join(__dirname, 'src', 'styles')],
  },
  images: {
    domains: ['static.2chaproj.com', 'picsum.photos'],
  },
  experimental: {
    scrollRestoration: true,
  },
};

module.exports = nextConfig;
