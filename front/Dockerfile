FROM nginx:1.27-alpine

COPY ./dist/bank-front-runtime/ /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
