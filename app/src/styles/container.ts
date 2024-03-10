import { StyleSheet } from "react-native";

export const container = StyleSheet.create({
  containerColumnSpaceBetween: {
    flex: 1,
    flexDirection: "column",
    justifyContent: "space-between",
  },
  containerColumnSpaceAround: {
    flex: 1,
    flexDirection: "column",
    justifyContent: "space-around",
  },
  containerColumnSpaceEvenly: {
    flex: 1,
    flexDirection: "column",
    justifyContent: "space-evenly",
  },
  containerRowSpaceBetween: {
    flex: 1,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  containerRowSpaceAround: {
    flex: 1,
    flexDirection: "row",
    justifyContent: "space-around",
    alignItems: "center",
  },
  containerRowSpaceEvenly: {
    flex: 1,
    flexDirection: "row",
    justifyContent: "space-evenly",
    alignItems: "center",
  },
  containerRow: {
    flexDirection: "row",
  },
  containerColumn: {
    flexDirection: "column",
  },
});
