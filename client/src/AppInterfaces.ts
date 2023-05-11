/**
 * Website information that the UI interacts with
 */
export interface WebsiteInfo {
  /**
   * byte64 encoded image
   */
  favicon: string;

  /**
   * Website title
   */
  title: string;

  /**
   * url to get to the website
   */
  url: string;
}