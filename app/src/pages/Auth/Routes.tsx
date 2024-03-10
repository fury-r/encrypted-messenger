import { createStackNavigator } from "@react-navigation/stack";
import { LoginScreen } from "./Login/Login";
import { RegisterScreen } from "./Register/Register";

const Stack = createStackNavigator();

export const AuthNavigation = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen name="Login" component={LoginScreen} />
      <Stack.Screen name="Register" component={RegisterScreen} />
    </Stack.Navigator>
  );
};
