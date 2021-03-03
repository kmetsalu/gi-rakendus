FROM node:lts-alpine
RUN npm install -g http-server
WORKDIR /gi-rakendus/frontend
COPY package*.json ./
RUN npm install --production
COPY . .
RUN npm run build
CMD ["http-server", "dist", "-p", "8081"]