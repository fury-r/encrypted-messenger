import { Text } from "react-native-paper";
import { StyleProp } from "react-native/types";
export const FontText = ({
  children,
  fontFamily,
  fontType,
  styles,
  ...rest
}: {
  children: any | any[];
  fontFamily?: "OpenSans";
  fontType?: "Regular" | "Bold" | "SemiBold" | "Italic" | "ExtraBold";
  styles?: StyleProp<any>;
}) => {
  const font = `${fontFamily ? fontFamily : "OpenSans"}-${
    fontType ? fontType : "Regular"
  }`;
  return (
    <Text
      style={{
        fontFamily: font,
        color: "black",
        ...styles,
      }}
      {...rest}
    >
      {children}
    </Text>
  );
};
