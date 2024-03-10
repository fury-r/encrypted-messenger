import { StyleSheet } from "react-native";

export const loginStyles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: "column",
    justifyContent: "space-between",
  },
  inputContent: {
    borderRadius: 10,
    backgroundColor: "white",
    borderWidth: 0,
  },

  inputContainer: {
    marginVertical: 8,
    border: 0,
  },

  buttonLogin: {
    borderRadius: 10,
    backgroundColor: "none",
    marginHorizontal: 8,
    borderWidth: 2,
    height: 55,
    borderColor: "white",
    flexDirection: "row",
    alignItems: "center",
  },
  pageButton: {
    backgroundColor: "white",
    borderRadius: 8,
    height: 45,
    width: "50%",
  },
});
