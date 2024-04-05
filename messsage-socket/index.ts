import { server } from "./src/socket";

const PORT = process.env.SERVER_PORT || 3001;
server.listen(PORT, () => {
  console.log(`Message Socket Server running on Port ${PORT} `);
});
